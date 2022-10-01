# Pixage

# Assumptions

1. Whenever app will start it will try to load images with the query 'fruits' but in case of no internet it will display the previously cached image list from the local db.
2. Whenever a search is performed with new query text, all the previously cached data is deleted and newly fetched list is cached and displayed.
3. I'm sending the code as zip file instead of github link because I put the gradle.properties file in .gitignore which contains my Pixabay api key.
