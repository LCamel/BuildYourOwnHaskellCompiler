#!/bin/sh
S=$1
touch $S
    perl -e 'print "\n" x 10'
while true; do
    NEWEST=`ls -tr $* | tail -n 1`
    [ $NEWEST -nt run.time ] && touch run.time && clear && perl -e 'print "\n" x 10' && node --harmony --harmony_destructuring $S;
    sleep 1;
done
