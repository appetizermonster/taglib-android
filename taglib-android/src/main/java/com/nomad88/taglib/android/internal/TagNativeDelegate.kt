package com.nomad88.taglib.android.internal

internal interface TagNativeDelegate {
    fun title(ptr: Long): String
    fun artist(ptr: Long): String
    fun album(ptr: Long): String
    fun albumArtist(ptr: Long): String
    fun genre(ptr: Long): String
    fun year(ptr: Long): Int
    fun track(ptr: Long): Int
    fun disc(ptr: Long): Int
    fun lyrics(ptr: Long): String
    fun coverArtFormat(ptr: Long): Int
    fun coverArtData(ptr: Long): ByteArray?
    fun setTitle(ptr: Long, title: String)
    fun setArtist(ptr: Long, album: String)
    fun setAlbum(ptr: Long, album: String)
    fun setAlbumArtist(ptr: Long, albumArtist: String)
    fun setGenre(ptr: Long, genre: String)
    fun setYear(ptr: Long, year: Int)
    fun setTrack(ptr: Long, track: Int)
    fun setDisc(ptr: Long, disc: Int)
    fun setLyrics(ptr: Long, lyrics: String)
    fun setCoverArt(ptr: Long, format: Int, data: ByteArray)
    fun deleteCoverArt(ptr: Long)
}
