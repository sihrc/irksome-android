# Irksome
NIST Reference Data Challenge

[Google Play Store](https://play.google.com/store/apps/details?id=io.sihrc.irksome)

NOTE: So far, only element data has been implemented from datasets: NIST SRD: 111, 144
NOTE: Pardon the lazy app naming

This product uses data provided by the National Institute of Standards and Technology (NIST) but is not endorsed or certified by NIST.

#### Description ####
Uses keywords API from indico.io for keyword detection in search query before performing the data lookup.

This app is a simple look-up application that tries to ease the burden of looking up reference data for the user by providing speech-to-text recognition and a search box.  Speech-to-text or search query submission triggers the look-up and takes the user to a page of results from the server that hosts the NIST reference data. The design of the app focuses on a simple user interface with as little user interaction and steps as possible to look-up the desired data.

On the server side, indico's keywords API uses the most significant terms in the query to perform a look-up on a comprehensive hash built from the reference data and returns the top 5 ~ 10 results. It also spell-corrects the query to the nearest words that exist in the server's query hash. More data from NIST can be incorporated into the look-up table to provide a broader scope of reference data. The time it takes perform the look-up is not significantly affected by the amount of data since most the data is preproccessed into the hash.

Both the app and the server are online and available for public use at the moment.

Thanks to freepik @ flaticon.com for the app icon
