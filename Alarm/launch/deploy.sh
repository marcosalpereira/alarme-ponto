#!/bin/bash

mavenExec=$1

pwd

currentDir=$(pwd)
distDir=$(basename $currentDir)

if [ "${distDir}" != "dist" ]; then
    echo "Nao estou no diretorio de distribuicao!!"
    exit 1   
fi

rm -vrf version-*

$mavenExec clean install -f ../pom.xml
if [ $? -ne 0 ]; then
   exit 1
fi

cd version-*

zip binario.zip *

workRoot=/tmp/ml-deploy.tmp
downloadDir=${workRoot}/download
workDir=${downloadDir}/v${version}

mkdir -p ${workRoot}

rm -vrf /tmp/ml-deploy.tmp/*

mkdir -p ${workDir}

mv binario.zip ${workDir}
echo "/marcosalpereira/alarm/releases/download/v${version}/binario.zip" > ${downloadDir}/latest

cd ${workRoot}
#scp -r * root@siscon.fla.serpro:/var/www/html/marcosalpereira/alarm/releases
