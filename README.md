Hello

This web application requires Linux OS (tested on Ubuntu 16.04), installed kvm and libvirt and a qcow2 VM disk image and a Tomcat 8 as Application server.

When server starts we can use /api/vm/start/<mac_address> to start a VM Domain with <mac_address>
specified by user.


On boot the said domain should send a request to the server, signalling it that the VM with <mac_adress> has started.
Script, which sends this request is located under \src\main\resources\testbirds-challenge-vm.sh


If user wants to check on VM status, it can be done by using /api/vm/status/<mac_address>, which should respond with message "VM: <vm_name> is <vm_state>" where VMs that have not responded are "initialised" and VMs that responded are "started"


To shut down a VM with <mac_address> use /api/vm/stop/<mac_address> - it should also remove generated resource file for this VM creation


