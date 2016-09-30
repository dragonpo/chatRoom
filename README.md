<h1 align="center">Chat Room</h1>

####################
#     CHATROOM     #
####################

Chat room server that will be able to support multiple platforms.
The chatroom server also wants to make it easy for not just users to
customize things, but also the Server Operators, Developers, and System Operators.

##############
# To Do List #
##############

* More System Operator Commands IN PROGRESS
* User Commands
* Room Operators IN PROGRESS
* Room Operator Commands IN PROGRESS
* Special Character Support
* More Connection Packet Features
* Plugin System
* Config File System IN PROGRESS

###########
# Ranking #
###########

* System Operator
* User (Can become a Room Operator, this is not a seperate rank just an extension)

##############
# Extra Info #
##############

* When a System Operator joins a room that is empty they are not made a room operator.
* When you join a room that has an already in use name, you are renamed.
* When someone joins with a name that includes a space, then it will will make your name into
your previous name minus the parts after the first space.

#########################################
# Need Help? Or Want To Ask A Question? #
#########################################

* Email: fusiondubstepz@gmail.com
* Facebook: https://www.facebook.com/noah.byrge.3
* Google Plus; https://plus.google.com/101541588713030258025

##################
# New Idea Hints #
##################
'''Java
for(User currentUser: Server.getAllUsers()) {
     if(currentUser.getRoom().equals(user.getRoom()) && currentUser.getSubroom().equals(user.getSubroom())) {
          currentUser.broadcast(Msg);
     }
}
'''
