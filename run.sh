#!/bin/sh
S=$1
touch $S
while true; do [ $S -nt run.time ] && touch run.time && clear && node --harmony --harmony_destructuring $S; sleep 1; done
