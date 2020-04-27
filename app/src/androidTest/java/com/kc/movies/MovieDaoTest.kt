package com.kc.movies

import android.content.Context
import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.kc.movies.database.MovieDao
import com.kc.movies.database.MovieDatabase
import com.kc.movies.model.Movie
import org.junit.*
import org.junit.rules.TestRule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MovieDaoTest{

    @Rule
    @JvmField
    val rule: TestRule = InstantTaskExecutorRule()

    private lateinit var database: MovieDatabase
    private lateinit var movieDao: MovieDao

    @Before
    fun setUp(){
        val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
        try{
            database = Room.inMemoryDatabaseBuilder(context, MovieDatabase::class.java).allowMainThreadQueries().build()
        } catch (e: Exception) {
            Log.i("Test", e.message)
        }
        movieDao = database.getMovieDao()
    }

    @Test
    fun addingAndRetrievingData(){
        val preInsertMovies = movieDao.getAllMovies()
        val movie = Movie(1)
        movieDao.insertMovie(movie)

        val postInsertMovies = movieDao.getAllMovies()
        //val sizeDifference: Int? = postInsertMovies.value?.size - preInsertMovies.value?.size
        //Assert.assertEquals(1, sizeDifference)
        //val retrievedId = postInsertMovies.last().id
        //Assert.assertEquals(1L, retrievedId)
    }

    @After
    fun tearDown(){
        database.close()
    }
}