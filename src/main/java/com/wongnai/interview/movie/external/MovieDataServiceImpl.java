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

		// Retrieve JSON as a string from MOVIE_DATA_URL using restTemplate
		String response = restTemplate.getForObject(MOVIE_DATA_URL,String.class);
		// Instantiate a MovieResponse object for returning back
		MoviesResponse moviesResponse = new MoviesResponse();
		// Use-try-catch to handle errors
		try {
			// Use objectMapper to convert JSON array (in response) to the list of MovieData
			// Then, add all of them into moviesResponse for returning back
			moviesResponse.addAll(Arrays.asList(objectMapper.readValue(response, MovieData[].class)));
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Return moviesResponse
		return moviesResponse;
	}

}
