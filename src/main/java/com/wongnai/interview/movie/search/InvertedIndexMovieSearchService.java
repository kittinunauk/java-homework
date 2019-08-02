package com.wongnai.interview.movie.search;

import java.util.*;
import java.util.stream.Collectors;

import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.wongnai.interview.movie.Movie;
import com.wongnai.interview.movie.MovieRepository;
import com.wongnai.interview.movie.MovieSearchService;

@Component("invertedIndexMovieSearchService")
@DependsOn("movieDatabaseInitializer")
public class InvertedIndexMovieSearchService implements MovieSearchService {
	@Autowired
	private MovieRepository movieRepository;

	@Override
	public List<Movie> search(String queryText) {
		//TODO: Step 4 => Please implement in-memory inverted index to search movie by keyword.
		// You must find a way to build inverted index before you do an actual search.
		// Inverted index would looks like this:
		// -------------------------------
		// |  Term      | Movie Ids      |
		// -------------------------------
		// |  Star      |  5, 8, 1       |
		// |  War       |  5, 2          |
		// |  Trek      |  1, 8          |
		// -------------------------------
		// When you search with keyword "Star", you will know immediately, by looking at Term column, and see that
		// there are 3 movie ids contains this word -- 1,5,8. Then, you can use these ids to find full movie object from repository.
		// Another case is when you find with keyword "Star War", there are 2 terms, Star and War, then you lookup
		// from inverted index for Star and for War so that you get movie ids 1,5,8 for Star and 2,5 for War. The result that
		// you have to return can be union or intersection of those 2 sets of ids.
		// By the way, in this assignment, you must use intersection so that it left for just movie id 5.

        // Fetch all movies into movieList for Inverted Index Construction
		ArrayList<Movie> movieList = Lists.newArrayList(movieRepository.findAll());
        // Inverted Index Construction <Term, Movie Ids>
        HashMap<String,ArrayList<Long>> invertedIndex = new HashMap<>();
        // Traverse all movies in movieList and put terms
		for(Movie movie : movieList){
		    // Split the title of a movie using a regular expression
            // to split each term in the title using any non-word character
            // between each term
            String[] tempMovieName = movie.getName().split("\\W+");
            // Traverse each term in the title of the movie
            for(String term : tempMovieName){
                // Convert term into lowercase since we need to handle case insensitive searching
                term = term.toLowerCase();
                // Check if term is already exists in inverted index
                // If it is already exists, append this movie id into its movie's id list
                // If not, put this word as a new entry and append this movie's id to its list
                // Assume that stop words can appear in a query. So, currently we should not
                // to eliminate them
                if(invertedIndex.containsKey(term)){
                    invertedIndex.get(term).add(movie.getId());
                }else{
                    invertedIndex.put(term, new ArrayList<Long>(Arrays.asList(movie.getId())));
                }

            }
		}

        // Split a queryText into terms
		String[] terms = queryText.toLowerCase().split("\\W+");
		// Instantiate a List of movie's ids that matched with the query
		List<Long> resultID = new ArrayList<>();

		// Check if the first term in the query match with any term in the inverted index
		if(invertedIndex.containsKey(terms[0])){
		    // In case there is such a term, store the movie's ids that has a title
            // contains that term as a first movie's id list
            ArrayList<Long> firstIDList =  invertedIndex.get(terms[0]);
            resultID.addAll(firstIDList);
        }

		// In case that there is one more terms in the query, we need to traverse all terms
        // in the query and find their corresponding list of movie's ids
        if(terms.length>1){
            for(String term : terms){
                // If we have this term in the inverted index, intersect the current list of
                // movie's ids with the one that this term matched in the inverted index.
                if(invertedIndex.containsKey(term)){
                    resultID = resultID.stream().distinct().filter(invertedIndex.get(term)::contains).collect(Collectors.toList());
                }
            }
        }

        // Instantiate the list containing Movie objects for returning
        List<Movie> result = new ArrayList<>();
        // Traverse the list of movie's ids that matched with the query
        // to find a Movie object using a movie's id as a key
        for(Long movieID : resultID){
            for(Movie tempMovie : movieList) {
                if(tempMovie.getId().equals(movieID)) {
                    result.add(tempMovie);
                }
            }
        }

        // Return the matched movies
		return result;
	}
}
