# London city bike availability status

This small app sends scheduled email of London city rental cycle availability.
I commute with the city cycle every morning but sometimes I end up with the station without any bikes left.
This app helps me choose which station to go to prevent time loss in the morning. 

## Usage

You would need to specify the ID of the biking dock station in the xpath query in the core.clj file. 
The way I find IDs is very primitive but going to the original xml and search for the bike station I use. Available here: https://tfl.gov.uk/tfl/syndication/feeds/cycle-hire/livecyclehireupdates.xml
If you are a xpath savvy person, you can perhaps write some magical xpath query to efficiently get the data you want. 
You would need to put in the details of your email and passwords.
You can also change the time email is sent according to your commute time. It is currently set to 6:50 AM which is roughly 10 min. before I o to work. x

## License

Copyright © 2016 Ryuei Sasaki

Distributed under the Apache License either version 2.0 or (at
your option) any later version.
