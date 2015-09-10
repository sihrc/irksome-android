rm ./*.apk
./gradlew assemblerelease

user_name=$(whoami)

if (($#==0)); then
    branch_name=$(git symbolic-ref -q HEAD)
    branch_name=${branch_name##refs/heads/}
    branch_name=${branch_name:-HEAD}
else
    branch_name=${1}
fi

DATE=`date +%m-%d@%H:%M`
name="Irksome-"${branch_name}"-production-${user_name}-"$DATE".apk"

mv "app/build/outputs/apk/app-release.apk" $name
