#!/bin/sh
#Start.sh by Guillem Pétriz
#Console menu and guide to the game

if [ $1 = "-h" ]; 
then
   echo "-t -> runs the AI training program"
   echo "-g -> runs the game"
elif [ $1 = "-g" ]; 
then
    if [ ! -f game.csv ];
    then
        echo "Can't run the game! You need to train the AI first"
    else
        python3 game.py
    fi
elif [ $1 = "-t" ]; 
then
    python3 training.py
else
    echo "Please, use the following structure: start.sh -[t] -[r] -[h]"  
fi
