read FILEPATH

if [ -e "$FILEPATH" ] #"[" is used as test command. remember space between "[" and parameters
then echo "valid"
else echo "invalid"

fi