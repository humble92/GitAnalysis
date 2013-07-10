# OpenStack
git clone https://github.com/openstack/quantum.git
git clone https://github.com/openstack/keystone.git
git clone https://github.com/openstack/glance.git
git clone https://github.com/openstack/horizon.git
git clone https://github.com/openstack/swift.git
git clone https://github.com/openstack/cinder.git
git clone https://github.com/openstack/nova.git
# OpenNebula
git clone git://git.opennebula.org/one.git
# Eucalyptus
git clone https://github.com/eucalyptus/eucalyptus.git
# CloudStack
#git clone https://github.com/apache/incubator-cloudstack.git
git clone https://git-wip-us.apache.org/repos/asf/cloudstack.git


cd eucalyptus
git log --no-merges > log.txt
cd ..

cd one
git log --no-merges > log.txt
cd ..

#cd cloudstack
#git log --no-merges > log.txt
#cd ..

cd cloudstack
git log --no-merges > log.txt
cd ..

cd nova
git log --no-merges > log.txt
cd ..

cd horizon
git log --no-merges > log.txt
cd ..

cd keystone
git log --no-merges > log.txt
cd ..

cd swift
git log --no-merges > log.txt
cd ..

cd cinder
git log --no-merges > log.txt
cd ..

cd glance
git log --no-merges > log.txt
cd ..

cd quantum
git log --no-merges > log.txt
cd ..
