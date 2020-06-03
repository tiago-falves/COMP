#! /usr/bin/bash

# check number of arguments
if [ "$#" -ne 1 ]; then
  echo
  echo "Usage:"
  echo "bash $0 <JMM_file_path>"
  echo
  exit 1;
fi

# get the current folder
directory=`pwd`

# extract the name of the -jar executable
executable="${directory##*/}"

# save full JMM file path
file_path="$1"

# extract JMM file portion
file_name="${file_path##*/}"

# extract only the class name
class_name="${file_name%.*}"

# build the compiler
gradle build

# compile the JMM file
java -jar "$executable".jar "$file_path"

# generate bytecode with jasmin
java -jar jasmin-2.4/jasmin.jar -d ./compiled/bytecode ./compiled/jasmin/"$class_name".j

# compile the files to be included
javac $(find ./compiled/include | grep .java) -d compiled/bytecode 2> /dev/null

# execute
java -cp "compiled/bytecode" $class_name