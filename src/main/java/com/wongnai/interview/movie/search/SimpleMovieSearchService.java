package com.wongnai.interview.movie.search;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.wongnai.interview.movie.external.MovieData;
import com.wongnai.interview.movie.external.MoviesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wongnai.interview.movie.Movie;
import com.wongnai.interview.movie.MovieSearchService;
import com.wongnai.interview.movie.external.MovieDataService;

@Component("simpleMovieSearchService")
public class SimpleMovieSearchService implements MovieSearchService {
	@Autowired
	private MovieDataService movieDataService;

	@Override
	public List<Movie> search(String queryText) {
		//TODO: Step 2 => Implement this method by using data from MovieDataService
		// All test in SimpleMovieSearchServiceIntegrationTest must pass.
		// Please do not change @Component annotation on this class

		// Declare List for returning query result
		List<Movie> result = new ArrayList<Movie>();
		// Fetch all movies in database into movieResponse
		MoviesResponse moviesResponse = movieDataService.fetchAll();
		// Instantiate a regular expression pattern for exact match of queryText (Case Insensitive)
		Pattern regExPattern = Pattern.compile(".*?\\b"+queryText+"\\b.*?",Pattern.CASE_INSENSITIVE);

		// Traverse each movies in MovieResponse
		for(MovieData movieData : moviesResponse){
			// Instantiate a regular expression matcher to check if current title
			// contain queryText or not (Case Insensitive)
			Matcher regExMatcher = regExPattern.matcher(movieData.getTitle());
			// If a movie's title is matched and queryText is not a full movie's name
			if(regExMatcher.matches() && !queryText.equals(movieData.getTitle())){
				// Convert each matched MovieData into Movie object
				Movie tempMovie = new Movie(movieData.getTitle());
				tempMovie.getActors().addAll(movieData.getCast());
				result.add(tempMovie);
			}
		}

		// Return list of matched movies
		return result;
	}
}
