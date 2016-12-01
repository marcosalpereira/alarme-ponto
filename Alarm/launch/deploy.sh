#!/bin/bash

version=$(zenity --entry --text "Numero da Versao" --title "Numero da Versao" --entry-text "0.12")
cd "version-${version}"

zip binario.zip *

workRoot=/tmp/ml-deploy.tmp
downloadDir=${workRoot}/download
workDir=/v${version}

mkdir -p ${workRoot}
rm -vrf ${workRoot}/*
mkdir -p ${workDir}

mv binario.zip ${workDir}
echo "/marcosalpereira/alarm/releases/download/v${version}/binario.zip" > ${workDir}/latest

cd ${workRoot}
scp -r * root@siscon.fla.serpro:/var/www/html/marcosalpereira/alarm/releases
