package com.testbirdsChallenge.rest;

import com.testbirdsChallenge.entity.VM;

import javax.ws.rs.core.Response;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VMServiceImpl implements VMService {

    // path to xml disk image file for domain creation
    private final URL DOMAIN_XML_PATH = this.getClass().getClassLoader().getResource("challenge-vm.xml");
            

    // map of initialized and running Domains
    private Map<String, VM> macAdressMap;

    public VMServiceImpl() {
        macAdressMap = new HashMap<>();
    }

    /*
    This method starts a new VM using a copy of original xml configuration file with changed name and MAC Adress
    so the user can run a VM with the preffered one. Name is autogenerated and cannot be set by user.
     */
    @Override
    public Response start(String macAdress) {
        if (macAdressMap.keySet().contains(macAdress)) {
            return Response.status(400).entity("domain with MAC adress:" + macAdress + " already running").build();
        }
        String targetPath = "";

        try {
            targetPath = DOMAIN_XML_PATH.getFile().substring(0, DOMAIN_XML_PATH.getFile().length() - 4) + macAdressMap.size() + ".xml";

            Files.copy(Paths.get(DOMAIN_XML_PATH.getFile()), Paths.get(targetPath));

            List<String> origLines = Files.readAllLines(Paths.get(String.valueOf(DOMAIN_XML_PATH)));

            List<String> newLines = new ArrayList<>();

            String domainName = "";

            for (String line : origLines) {
                if (line.contains("<name>")) {
                    String[] lineSection = line.split("</name>");
                    line = lineSection[0] + macAdressMap.size() + "</name>";
                    domainName = line.substring(8, line.length() - 7);
                }
                if (line.contains("mac address")) {
                    String[] lineSection = line.split("=");
                    line = lineSection[0] + "='" + macAdress + "'" + "/>";
                }
                newLines.add(line);
            }

            macAdressMap.put(macAdress, new VM(macAdress, domainName, targetPath));

            Files.write(Paths.get(targetPath), newLines);

        } catch (IOException e) {
            return Response.status(400).entity("failed to create a resourse file " +macAdress).build();
        }
        Response responseDomain = sendRequest("virsh",
                "create " + targetPath,
                "failed to start VM: " + macAdress);

        if (responseDomain.getStatus() == 200) {
            return Response.ok().entity("started VM: " + macAdress).build();
        }

        return responseDomain;
    }


    /*
    Method for running Terminal command for libvirt to shutdown or start the domain specified by its MAC Adress.
    This could be done with libvirt Java binding, but i haven't figured out how to initialise Connect object correctly
    so i used Runtime.
     */
    private Response sendRequest(String command, String argument, String softFailMessage) {
        try {
            Process createDomainProcess = new ProcessBuilder(command, argument).start();
            createDomainProcess.waitFor();
            if (createDomainProcess.exitValue() != 0) {
                byte[] bytes = createDomainProcess.getErrorStream().readAllBytes();
                StringBuilder file_string = new StringBuilder();

                for (byte aByte : bytes) {
                    file_string.append((char) aByte);
                }

                return Response.ok().entity(file_string.toString()).build();
            }
            return Response.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.ok().entity(softFailMessage).build();
    }

    /*
    This method stops the domain specified by its MAC Adress and removes the copy of original
     */
    @Override
    public Response stop(String macAdress) {
        VM virtualMachine = macAdressMap.get(macAdress);
        if (virtualMachine == null) {
            return Response.status(400).entity("failed to find VM with MAC Adress: " + macAdress).build();
        }
        sendRequest("virsh",
                "destroy " + virtualMachine.getName(),
                "failed to stop VM: " + macAdress);

        try {
            Files.delete(Paths.get(virtualMachine.getResourcePath()));
        } catch (IOException e) {
            return Response.status(400).entity("failed to remove VM resourse file " + virtualMachine.getResourcePath()).build();
        }
        macAdressMap.remove(virtualMachine.getMacAdress());
        return Response.ok().entity("stop " + virtualMachine.getName()).build();
    }

    /*
    Method returns status of a VM specified by its MAC Adress
     */
    @Override
    public Response status(String macAdress) {

        VM virtualMachine = macAdressMap.get(macAdress);
        if(virtualMachine==null){
            return Response.status(400).entity("failed to find VM with MAC Adress: " + macAdress).build();
        }
        return Response.ok().entity("VM: " + virtualMachine.getName() + " is " + virtualMachine.getState()).build();
    }


    /*
    Method that recieves a request from VM with MAC Adress of a booted Domain as parametr
     */
    @Override
    public Response update(String macAdress) {
        VM virtualMachine = macAdressMap.get(macAdress);
        virtualMachine.setState("booted");
        macAdressMap.put(virtualMachine.getMacAdress(),virtualMachine);
        return Response.ok().entity("VM: " + virtualMachine.getName() + " state updated ").build();
    }
}
