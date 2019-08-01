package com.wongnai.interview.movie.sync;

import javax.transaction.Transactional;

import com.wongnai.interview.movie.Movie;
import com.wongnai.interview.movie.external.MovieData;
import com.wongnai.interview.movie.external.MoviesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wongnai.interview.movie.MovieRepository;
import com.wongnai.interview.movie.external.MovieDataService;

@Component
public class MovieDataSynchronizer {
	@Autowired
	private MovieDataService movieDataService;

	@Autowired
	private MovieRepository movieRepository;

	@Transactional
	public void forceSync() {
		//TODO: implement this to sync movie into repository

		// Fetch all movies into moviesResponses
		MoviesResponse moviesResponse = movieDataService.fetchAll();
		// Traverse each movie in MovieResponse
		for(MovieData movieData : moviesResponse){
			// Instantiate a temporary movie object to put movie's data in it
			// and for load it into repository
			Movie tempMovie = new Movie(movieData.getTitle());
			tempMovie.getActors().addAll(movieData.getCast());
			// Add a movie instance into MovieRepository
			movieRepository.save(tempMovie);
		}

	}
}
