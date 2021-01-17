# MemeFlickr
Premise -> Scroll endlessly through memes from reddit. A meme never shows up twice. Download and share functionalities implemented


Language: Kotlin

Technologies: Retrofit, RealmDB, Paging3, Glide, Dagger Hilt

API: none, just getting posts from https://www.reddit.com/r/memes.json


# Logic:

The JSON response contains a batch of posts (20+) and a "next" key which is used to get the next batch.
The problem with loading memes purely from that response was that memes repeated when restarting the app, and even in different batches.
The solution was to use a local database that stores memes. These stored memes have a "seen" bool value, which become true when they're seen on screen (onBindViewHolder). 
This sort of meme source is very inconvenient because when the user restarts the app (especially in a short period), there could be no new memes in the first few batches, which resulted in the creating of special solutions that on rare occasions show already seen memes. 
Sharing just takes the url of the image (for now), so it's only reliable for platforms like Discord that automatically parse it. 
The download is done using DownloadManager that takes the URI parsed url of the meme. 



#### Sidenote

Paging is a HUGE overkill in this app and had infact caused some problems with loading same memes which required special solutions.



# TODOs


#1 Upgrade the share functionality so it can share the meme on any platform.

#2 Add the option for the user to choose which subbreddit to get the memes (or general posts) from.

#3 Make the UI less intrusive for the image



# VERY optimistic TODOs

#1 Add a search functionality that displays results if matched with text from within the image, title, description, etc..


# Showcase
![GIF showcase of the app](https://cdn.discordapp.com/attachments/707574253116981274/800330886003294208/20210117-121117_5.gif)
