#!/usr/bin/env bash

exec &>> test.txt

oldIFS=${IFS}
IFS=$'\n'

# printf "IFS was: ${oldIFS}"

find ./submissions/ -iname '* *' | while read LINE
do
  newName="${LINE// /_}"
  printf "removing space from ${LINE} new name ${newName}\r\n"
  mv "${LINE}" "${newName}"
done

find ./submissions/ -type f -iname '* *' | while read LINE
do
  newName="${LINE// /_}"
  printf "removing space from ${LINE} new name ${newName}\r\n"
  mv "${LINE}" "${newName}"
done