# MemeFlickr
Scroll endlessly through memes from reddit. A meme never shows up twice. Download and share functionalities implemented

Language: Kotlin
Technologies: Retrofit, Room, Paging3, Glide, Dagger Hilt
API: none, just getting json responses from /r/memes.json

Logic:
JSON response contains a batch of posts (20+) and a "next" key which is used to get the next batch. The problem with loading memes purely from that response was that memes repeated when restarting the app, and even in different batches. The solution was to use a local database that stores memes. These memes have a "seen" bool value, which become true when they're seen on screen (onBindViewHolder). 
This sort of meme source is very inconvenient because when the user restarts the app (especially in a short period), there could be no new memes in the first few batches, which resulted in the creating of special solutions that on rare occasions show already seen memes. 
Sharing just takes the url of the image (for now), so it's only reliable for platforms like Discord that automatically parse it. 
The download is done using DownloadManager that takes the URI parsed url of the meme. 

TODOs:
Upgrade the share functionality so it can share the meme on any platform.
Add the option for the user to choose which subbreddit to get the memes (or general posts) from.
Make the UI less intrusive for the image

VERY optimistic TODOs:
Add a search functionality that displays results if matched with text from the image, title, description, etc..


![GIF howcase of the app](https://cdn.discordapp.com/attachments/707574253116981274/800323449192972308/20210117-121117_2.gif)
