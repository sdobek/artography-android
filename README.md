artography-android
==================

the android app that shows you street art in your area


Problem statement: People that appreciate street art need a way to connect with their community’s art, based on location and newness of the piece. There is currently no very good way to do this.

Solution: “Artography” is like a yelp for street art. You can explore the street art around you, see details about the artist/artists piece, and add new art sites that you discover. 

Basic User Story:
- User logs in with their facebook account.(this will eliminate the need to create a ‘create account’ flow) 
- For the home screen they see a feed of recently added street art styled exactly like in the airbnb app.
- They click on an icon we can "borrow" from foursquare which takes them to a map. This map will display art in their current vicinity in a yelp like fashion
- If the user finds a previously undocumented piece of art, they can document it
- They can report if a piece of art has been removed at which point we can archive it. 
- Each piece of art can be given an up or down rating, and have more pictures of it submitted. 

------------------------------------------------(Wishfulfeatures)------------------------------------------------

- mark recent pieces of art with a special "fresh" marker

- Each piece of art could be "checked into"

- Create and explore art trails: Users can select a trail of checkpoints and check off or check-in to art sites as they visit them. They get a badge when they finish a full trail.

- if there is extra time they could filter art by "tags" or "categories" in a search

- if a user really likes the art, they can "favorite" it and keep those favorites stored. 

- potential verification for the actual artist who made the piece to comment on it. 

Installing FacebookSDK
----------------------
1. Download FacebookSDK from [here](https://developers.facebook.com/docs/android/)
2. Import the FacebookSDK into Eclipse (you don't need to import the sample projects)
3. Right click on kARTography. Select Properties->Android
  1. Under library, click "Add," and select "FacebookSDK"
  2. Make sure "Is Library" is not selected for kARTography
4. You may get an error that there are multiple android-support-v4.jar files.  To solve this, copy the one in kARTography, delete the one in FacebookSDK, and paste the kARTography one in FacebookSDK.  They will now be using the same jar.

