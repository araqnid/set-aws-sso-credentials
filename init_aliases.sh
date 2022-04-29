script="$1"

aws_sso_profile() {
    eval $( node "$script" "$1" )
    aws sts get-caller-identity
}

echo -n "AWS SSO aliases:"

node "$script" | while read profile; do
  if [ "$profile" = "default" ]; then
    true
  else
    echo -n " $profile"
    eval "alias $profile='aws_sso_profile $profile'"
  fi
done

echo "."
