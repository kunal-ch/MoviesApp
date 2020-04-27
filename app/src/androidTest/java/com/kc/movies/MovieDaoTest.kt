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
    fun validIdTest(){
        val movie = Movie(1)
        database.getMovieDao().insertMovie(movie)
        val postInsertMovies: Movie? = database.getMovieDao().getAllMovies().value?.first()
        Assert.assertEquals(movie.id, postInsertMovies?.id)
    }

    @Test
    fun inValidIdTest(){
        val movie1 = Movie(1)
        val movie = Movie(2)
        database.getMovieDao().insertMovie(movie)
        val postInsertMovies: Movie? = database.getMovieDao().getAllMovies().value?.first()
        Assert.assertNotEquals(movie1.id, postInsertMovies?.id)
    }

    @Test
    fun validTitleTest(){
        val movie = Movie(1, title= "Spiderman")
        database.getMovieDao().insertMovie(movie)
        val postInsertMovies: Movie? = database.getMovieDao().getAllMovies().value?.first()
        Assert.assertEquals(movie.title, postInsertMovies?.title)
    }

    @Test
    fun inValidTitleTest(){
        val movie1 = Movie(1, title= "Spiderman")
        val movie = Movie(1, title= "Batman")
        database.getMovieDao().insertMovie(movie)
        val postInsertMovies: Movie? = database.getMovieDao().getAllMovies().value?.first()
        Assert.assertEquals(movie1.title, postInsertMovies?.title)
    }

    @After
    fun tearDown(){
        database.close()
    }
}