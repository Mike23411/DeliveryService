#!/usr/bin/env bash

exec &>> results.txt

now=$(date +"%m-%d-%y %r")
echo "Start time : $now"

oldIFS=${IFS}
IFS=$'\n'

# printf "IFS was: ${oldIFS}"

find ./submissions/ -iname '* *' | while read LINE
do
  newName="${LINE// /_}"
  printf "removing space from ${LINE} new name ${newName}\r\n"
  mv "${LINE}" "${newName}"
done

find ./submissions/ -type f -name '* *' | while read LINE
do
  newName="${LINE// /_}"
  printf "removing space from ${LINE} new name ${newName}\r\n"
  mv "${LINE}" "${newName}"
done

IFS=${oldIFS}

for i in $(find ./submissions/ -type f -iname "*.pdf")
do
  printf "removing ${i}\r\n"
  rm -f "${i}"
done

for i in $(find ./submissions/ -type f -iname "*.pdf")
do
  printf "deleting ${i}\r\n"
  delete "${i}"
done

for i in $(find ./submissions/ -type f -iname "*.jpg")
do
  printf "removing ${i}\r\n"
  rm -f "${i}"
done

for i in $(find ./submissions/ -type f -iname "*.jpg")
do
  printf "deleting ${i}\r\n"
  delete "${i}"
done

for i in $(find ./submissions/ -type f -iname "*.jpeg")
do
  printf "removing ${i}\r\n"
  rm -f "${i}"
done

for i in $(find ./submissions/ -type f -iname "*.jpeg")
do
  printf "deleting ${i}\r\n"
  delete "${i}"
done

for i in $(find ./submissions/ -type f -iname "*.png")
do
  printf "removing ${i}\r\n"
  rm -f "${i}"
done

for i in $(find ./submissions/ -type f -iname "*.png")
do
  printf "deleting ${i}\r\n"
  delete "${i}"
done

for i in $(find ./submissions/ -type f -iname "*.txt")
do
  printf "removing ${i}\r\n"
  rm -f "${i}"
done

for i in $(find ./submissions/ -type f -iname "*.txt")
do
  printf "deleting ${i}\r\n"
  delete "${i}"
done

for i in $(find ./submissions/ -type f -iname "*.docx")
do
  printf "removing ${i}\r\n"
  rm -f "${i}"
done

for i in $(find ./submissions/ -type f -iname "*.docx")
do
  printf "deleting ${i}\r\n"
  delete "${i}"
done

# find ./submissions/ -type f -iname "*.jar" -execdir sh -c 'printf "%s\n" "${0%.*}"' {} ';' | xargs rm -r -f

mkdir ./submissions/jars

for i in $(find ./submissions/ -type f -iname "*.jar")
do
  name=${i/.\//}
  name=${name/submissions\//}
  name=${name/.jar/}
  printf "making jar folder ${name}\r\n"
  mkdir "./submissions/jars/${name}"
done

for i in $(find ./submissions/ -type f -iname "*.jar" -execdir sh -c 'printf "%s\n" "${0%.*}"' {} ';')
do
  name=${i/.\//}
  name=${name/submissions\//}
  name=${name/.jar/}
  printf "moving jar ${name}\r\n"
  mv "./submissions/${name}.jar" "./submissions/jars/${name}/"
done

for i in $(find ./submissions/ -type f -iname "*.zip" -execdir sh -c 'printf "%s\n" "${0%.*}"' {} ';')
do
  name=${i/.\//}
  name=${name/submissions\//}
  name=${name/.zip/}
  printf "removing old folder ${name}\r\n"
  rm -r -f ${name}
done

for i in $(find ./submissions/ -type f -iname "*.zip" -execdir sh -c 'printf "%s\n" "${0%.*}"' {} ';')
do
  name=${i/.\//}
  name=${name/submissions\//}
  name=${name/.zip/}
  printf "unzipping ${name}\r\n"
  unzip -d "./submissions/${name}/" "./submissions/${name}.zip"
done

for i in $(find ./submissions/ -type f -iname "*.zip" -execdir sh -c 'printf "%s\n" "${0%.*}"' {} ';')
do
  name=${i/.\//}
  name=${name/submissions\//}
  name=${name/.zip/}
  printf "moving zip ${name}\r\n"
  mv "./submissions/${name}.zip" "./submissions/${name}/"
done

# Remove spaces again
oldIFS=${IFS}
IFS=$'\n'

# printf "IFS was: ${oldIFS}"

find ./submissions/ -iname '* *' | while read LINE
do
  newName="${LINE// /_}"
  printf "removing space from ${LINE} new name ${newName}\r\n"
  mv "${LINE}" "${newName}"
done

# file type removed for spaces in directory structure.
find ./submissions/ -name '* *' | while read LINE
do
  newName="${LINE// /_}"
  printf "removing space from ${LINE} new name ${newName}\r\n"
  mv "${LINE}" "${newName}"
done

IFS=${oldIFS}

# End remove spaces again

# remove any macos folders or files.
for i in $(find ./submissions/ -type d -iname "__MACOSX")
do
  printf "removing ${i}\r\n"
  rm -rf "${i}"
done

for i in $(find ./submissions/ -type d -iname "__MACOSX")
do
  printf "deleting ${i}\r\n"
  delete "${i}"
done

for i in $(find ./submissions/ -type f -iname ".DS_Store")
do
  printf "removing ${i}\r\n"
  rm -f "${i}"
done

for i in $(find ./submissions/ -type f -iname ".DS_Store")
do
  printf "deleting ${i}\r\n"
  delete "${i}"
done

j=0

for i in $(find ./submissions/ -iname "src");
do
  # remove any existing containers
  docker ps -a -q | xargs docker rm -f &> /dev/null
  docker container prune -f &> /dev/null
  # remove any existing images
  docker rmi -f gatech/dronedelivery
  # setup next student
  folder=${i//src/}
  folder=${folder//SRC/}
  rmFolder=${folder//.\//}
  semesterFolder="2021-10-A3"
  # printf "src folder: ${folder}\r\n"
  # printf "src folder: ${rmFolder}\r\n"
  rm -rf "${rmFolder}test_scenarios/"*
  rm -rf "${rmFolder}test_results/"*
  rm -rf "${rmFolder}docker_results/"*
  rm -rf "${rmFolder}submission/"*
  rm -rf "${rmFolder}bin/"*
  rm -rf "${rmFolder}.idea/"*
  rm -rf "${rmFolder}scripts/"*
  rm -rf "${rmFolder}Dockerfile"
  rm -rf "${rmFolder}.classpath"
  rm -rf "${rmFolder}.gitignore"
  rm -rf "${rmFolder}README.md"
  rm -rf "${rmFolder}.project"
  cp "./${semesterFolder}/Dockerfile" "${folder}"
  cp "./${semesterFolder}/DockerfileV2" "${folder}"
  cp -R "./${semesterFolder}/scripts/." "${folder}scripts/"
  cp -R "./${semesterFolder}/test_scenarios/." "${folder}test_scenarios/"
  cp -R "./${semesterFolder}/test_results/." "${folder}test_results/"
  cDir=${PWD}
  cd "$folder"
  printf "${PWD}\r\n"
  # change default print to file
  exec &>> "results_${j}.txt"
  # build & run new student
  docker build -t gatech/dronedelivery -f Dockerfile ./ --no-cache --rm
  # rename old jar file
  # mv submission/streaming_wars.jar submission/old.jar
  # scripts/copy.sh
  # slightly higher than the grader 400 vs 450
  timeout 450 scripts/grader.sh
  cd "$cDir"
  exec &>> results.txt
  j=$((j+1))
done

exec &>> results.txt

now=$(date +"%m-%d-%y %r")
echo "Finish time : $now"