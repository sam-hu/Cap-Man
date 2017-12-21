# Cap-Man
A Pac-Man style game in Java complete with AI enemies and score-keeping, with 
the added twist that enemies will reproduce and die off with time  
(Inspired by Fallout 4)

How to Run This Game
--------------------------------------------------------------------------------
Download the files and run CapMan.jar

How to Play This Game
--------------------------------------------------------------------------------
You control the **Boy** using WASD. Your objective is to collect as many **Caps** as you
can. Each cap increments your score by one point. The game ends once you collect
all of the caps, or you are eaten by a **Bug**.

The enemy bugs start off **orange**. In this stage, they wander around the map 
blindly. After an amount of time, the orange bugs will turn **blue**. Blue bugs are 
able to spawn more orange bugs at random, and will pursue the Boy more 
aggressively. After another amount of time, the blue bug will die and disappear
off the map.

There are powerups scattered across the map that will give you an advantage.
There are **+10** powerups, which will add 10 to your score. There are also **Batteries**.
After picking up a battery, the Boy will turn red for a short period of time. 
During this time the Boy becomes invincible and can eat bugs to increase his 
score. Powerups will spawn at random.

About This Game
--------------------------------------------------------------------------------
This is my first ever CS project, developed in Spring 2016 as my final project 
for AP Computer Science. It's far from perfect, but an achievement nonetheless.

Known Issues
--------------------------------------------------------------------------------
The game can get slow if there are many bugs on-screen at once. 
