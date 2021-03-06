## Site hosted here: https://timmies-hockey-helper.herokuapp.com/


## What is Timmies Hockey Helper?
Haven't heard of the Tim Horton's Hockey Challenge? Check it out here: https://www.timhortons.ca/tims-nhl-hockey-challenge

This web-application is an unoffical companion app for the Tim Hortons Hockey Challenge. If you're like me and don't watch a lot of hockey but still want to make some smart picks and win free coffee, you're in the right place. This app will help you determine which players have the most total goals, the best goal per game averages, and the most goals out of their last 3 and 5 games. 


## How do I use this app?
I strongly recommend using a desktop browser with this app as it will greatly reduce the amount of time it takes to enter players. Here are the step-by-step instructions:
1. Open the Tim Hortons mobile app and enter the Tim Hortons Hockey Challenge.
2. Visit the wesite linked above.
3. Ensure the group of players is empty. If not, click on the "Clear All" button.
4. For each player in the first group: 
  * Type out their name until it appears in the select box
  * Hit enter to add their name to the group
5. Click the "Generate Best Picks" button
6. Use the provided data to pick a player and enter it into the Tim Hortons mobile app
7. Go back to the player entry page
8. Repeat steps 3-7 for each group of players


## How is this possible?
- API used to get NHL teams, players, and stats can be accessed here: https://gitlab.com/dword4/nhlapi
- Bauer font used for the title and headers can be found here: https://www.fontspace.com/bauer-font-f5060 
- Site is currently hosted using Heroku, read more here: https://www.heroku.com/


## Updates planned:
- [X] Update the layout so it is usable on mobile devices
- [ ] Currently, all users share the same list of selected players, making this web-app unusable for multiple people at the same time. This will be fixed once I find out the "Spring" way of doing this (I'm guessing I'll need to implement some sort of session/cookie storage solution)
- [ ] There needs to be a step-by-step list of instructions for how to use the app efficiently, either as a separate page or on the index.html for new users. 
- [ ] I'd like to compare a player's goals/game for their last X games to their season's goals/game to see if they're on a hot-streak or a cold-streak
- [ ] Create and add a favicon
- [ ] Add option to limit player list so only players on teams playing that day appear


#### Have any questions or suggestions for improvements? Send me an email at fedele.fernando1996@gmail.com
