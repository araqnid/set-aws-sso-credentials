script="$1"

aws_sso_profile() {
    eval $( node "$script" "$@" )
}

definitionsfile=$( mktemp )

echo -n "AWS SSO aliases:"

node "$script" | while read profile; do
  if [ "$profile" = "default" ]; then
    true
  else
    echo -n " $profile"
    echo "$profile() { aws_sso_profile $profile; }" >> "$definitionsfile"
  fi
done

. "$definitionsfile"

echo "."
