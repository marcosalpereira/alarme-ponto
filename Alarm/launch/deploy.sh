#!/bin/bash

mavenExec=$1
versionType=$2

app=alarm

if [ "${versionType}" != "latest" ] && [ "${versionType}" != "beta" ]; then
    echo "Versao tem que ser latest ou beta!"
    exit 1   
fi

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

currentDir=$(pwd)
filename=$(basename $currentDir)
version="${filename##*-}"

zip binario.zip *

rm -vrf /tmp/ml-deploy.tmp/*

workRoot=/tmp/ml-deploy.tmp
releasesDir=${workRoot}/.marcosalpereira/${app}/releases
downloadDir=${releasesDir}/download
mkdir -p ${downloadDir}/v${version}

mv binario.zip ${downloadDir}/v${version}
echo "/.marcosalpereira/$app/releases/download/v${version}/binario.zip" > ${releasesDir}/$versionType

cd ${workRoot}
scp -r ./. root@siscon.fla.serpro:/var/www/html
