package com.wongnai.interview.movie.external;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.wongnai.interview.movie.MovieRepository;
import jdk.nashorn.internal.parser.JSONParser;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

@Component
public class MovieDataServiceImpl implements MovieDataService {
	public static final String MOVIE_DATA_URL
			= "https://raw.githubusercontent.com/prust/wikipedia-movie-data/master/movies.json";

	@Autowired
	private RestOperations restTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public MoviesResponse fetchAll() {
		//TODO:
		// Step 1 => Implement this method to download data from MOVIE_DATA_URL and fix any error you may found.
		// Please noted that you must only read data remotely and only from given source,
		// do not download and use local file or put the file anywhere else.

		// Instantiate an object (MoviesRespones) for returning back
		MoviesResponse moviesResponse = new MoviesResponse();
		// Declare inputStream to read JSON in MOVIE_DATA_URL
		InputStream inputStream = null;
		// Use try-catch to handle errors
		try {
			// Open stream and put it into inputStream
			inputStream = new URL(MOVIE_DATA_URL).openStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Declare ReadingContext and initialize it using parsed information from inputStream
		ReadContext context = JsonPath.parse(inputStream);
		// Parse all movie objects into contextList for creating MovieResponse
		List<LinkedHashMap> contextList = context.read("$.*");
		// Instantiate a list to store parsed movie's data
		List<MovieData> movieDataList = null;

		try {
			// Use objectMapper to read JSON and parse read contents into MovieData objects.
			// Then, store them in movieDataList
			movieDataList = Arrays.asList(objectMapper.readValue(contextList.toString(), MovieData[].class));
			// Add all elements in  movieDataList (parsed movie's data) into moviesResponse for returning
			moviesResponse.addAll(movieDataList);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Return moviesResponse
		return moviesResponse;
	}

}
