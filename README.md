game
====

This project implements the game of Breakout.

Name: Jorge Alejandro Raad

### Timeline

Start Date: 1/14/19

Finish Date: 1/21/19

Hours Spent: 30

### Resources Used
 StackOverflow, Oracle, Tutorialspoint

### Running the Program

Main class: RunBreakOut

Data files needed: 
Level read-ins (integer refers to number of hits before brick breaks, other characters are placeholders)
1.txt
2.txt
3.txt

Brick images
brick1.gif
brick2.gif
brick3.gif

Bouncer images
color_ball.gif
happyface.gif
shiny_blue.gif

Power-ups/items
enlarger.gif
freeze.gif
multiplier.gif
poison.gif

Paddle
red_paddle.gif

Other visual elements
pause.gif
game_over.gif
graphic_box.gif
heart.gif
poison_bouncer.gif
title_art.gif


Key/Mouse inputs:

Cheat keys:
- Spacebar - pause
- 1 - starts up level 1
- 2 - starts up level 2
- 3 - starts up level 3
- A - add another life
- S - toggle “floor” so bouncer can’t fall off

Known Bugs:
- The bouncer often bounces off the paddle incorrectly or gets stuck a second before bouncing off.
- When poison mode is activated, a life is lost continually until it is over, whether a bouncer is touching the paddle or not.
- At times the freeze power-up speeds the bouncer up too much when it returning to normal.

Extra credit:


### Notes
Unaccomplished plans
- Poison mode at end of level
- Explosive block
- Hot streak

Features
- Cheat keys
- Number of lives displayed
- Score
- 3 types of blocks
- Speed up every level
- Select bouncer appearance
- Power-ups
    - Ball multiplier
    - Freeze power-up
    - Paddle-enlarging
- Poison mode(doesn’t work correctly)
- Bouncer bounces off paddle differently depending on location of hit

The app can be run by simply running RunBreakOut. The other file, RunGame, was the original class. RunGame runs the game without using any other classes. I realized I should focus more on the design, so I spent a lot of hours rewriting it in RunBreakOut by using several classes (Block, Bouncer, Item, Level, Paddle). 

If you look in the resources folder, you will find a lot of .gif and .paint files. Every one of these files was created by me using Paint S to design the visual elements of the game. Additionally, there are 3 .txt files that contain the information for placing the blocks within the level. Within the text files, a “1” corresponds to a 1-hit brick, a “2” to a 2-hit brick, and so on.

Probably the most confusing part of my code is the return arguments for the method “stepThrough” within the Level Class. It returns a -1 if a life was lost by the ball falling out of the screen, a -2 if a life was lost due to poison mode, and the score otherwise. This is because the level class does not have access to the overall lives remaining. I thought it was too complicated to have the number of lives be brought into the level and keeping track of it, so I thought this would be simpler to implement although unintuitive.

Also, although I implemented a way of the game restarting after 5 seconds in RunGame, I did not do that in RunBreakOut. When you win, nothing happens.
### Impressions
I actually really enjoyed this project. I have always wanted to make a game, and this was a great way to learn javafx. Maybe some more guidance on how to use javafx would have helped out in the beginning, but I believe that you learn most through working on an assignment.

