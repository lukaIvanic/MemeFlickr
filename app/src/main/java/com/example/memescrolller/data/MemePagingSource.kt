package com.example.memescrolller.data

import android.util.Log
import androidx.paging.PagingSource
import com.example.memescrolller.api.MemeApi
import io.realm.Realm
import io.realm.kotlin.where
import retrofit2.HttpException
import java.io.IOException
import kotlin.random.Random

private const val MEME_STARTING_PAGE_INDEX = 1

class MemePagingSource(val memeApi: MemeApi) : PagingSource<Int, Meme>() {

    var realm: Realm = Realm.getDefaultInstance()
    private var next: String = ""
    private var count: Int = 0


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Meme> {
        val position = params.key ?: MEME_STARTING_PAGE_INDEX
        (
                (
                        return try {
                            val response = memeApi.getMeme(next)
                            val memes: ArrayList<Meme> = ArrayList()

                            next = response.data.after

                            count++

                            Log.i("LOAD", "#$count")


                            for (post in response.data.children) {
                                // Checks if the post url is of the correct type
                                if (post.data.post_hint == "image" && !post.data.url.endsWith("gifv")) {
                                    // Checks if
                                    val dunnoMeme =
                                        realm.where<Meme>().equalTo("url", post.data.url)
                                            .findFirst()

                                    // If meme already exists in the db but hasn't been seen, add it to the rec view list
                                    if (dunnoMeme == null || !dunnoMeme.seen) {
                                        val currMeme = Meme(url = post.data.url, title = post.data.title)
                                        // ID generation
                                        val maxValue = realm.where<Meme>().max("id")
                                        var id = 0
                                        if (maxValue != null) {
                                            id = maxValue.toInt() + 1
                                        }
                                        currMeme.id = id

                                        // Transactions
                                        memes.add(currMeme)

                                        if (dunnoMeme == null) {
                                            realm.beginTransaction()
                                            realm.copyToRealm(currMeme)
                                            realm.commitTransaction()
                                        }
                                    }
                                }

                            }

                            // if memes stay empty do the following -> attempt to get not seen memes from realm -> attempt to get any memes from realm -> insert default meme
                            if (memes.isEmpty()) {
                                getMemesFromRealm(memes)
                                if(memes.isEmpty()){
                                    val temp = realm.where<Meme>().findAll()
                                    if(!temp.isNullOrEmpty()) {
                                        memes.add(temp[Random.nextInt(temp.size)]!!)
                                    }else{
                                        memes.add(Meme(url = "http://www.jollausers.com/wp-content/uploads/2013/08/psycho-meme-generator-phew-time-for-a-well-deserved-break-1d72ce.jpg"))
                                    }
                                }
                            }

                            LoadResult.Page(
                                data = memes,
                                prevKey = if (position == MEME_STARTING_PAGE_INDEX) null else position - 1,
                                nextKey = position + 1
                            )

                        } catch (exception: IOException) {
                            LoadResult.Error(exception)
                        } catch (exception: HttpException) {
                            LoadResult.Error(exception)
                        }
                        )
                )
    }

    private fun getMemesFromRealm(memes: ArrayList<Meme>) {
        val unseenMemes = realm.where<Meme>().limit(2).findAll()
        if (unseenMemes != null) {
            for (meme in unseenMemes) {
                if (!meme.seen) {
                    memes.add(meme)
                }
            }
        }

    }

}