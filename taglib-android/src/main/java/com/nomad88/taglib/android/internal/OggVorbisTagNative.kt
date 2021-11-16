package com.nomad88.taglib.android.internal

internal object OggVorbisTagNative : TagNativeDelegate {

    init {
        TagLib.ensureLoaded()
    }

    external override fun title(ptr: Long): String
    external override fun artist(ptr: Long): String
    external override fun album(ptr: Long): String
    external override fun albumArtist(ptr: Long): String
    external override fun genre(ptr: Long): String
    external override fun year(ptr: Long): Int
    external override fun track(ptr: Long): Int
    external override fun disc(ptr: Long): Int
    external override fun lyrics(ptr: Long): String
    external override fun coverArtFormat(ptr: Long): Int
    external override fun coverArtData(ptr: Long): ByteArray?
    external override fun setTitle(ptr: Long, title: String)
    external override fun setArtist(ptr: Long, album: String)
    external override fun setAlbum(ptr: Long, album: String)
    external override fun setAlbumArtist(ptr: Long, albumArtist: String)
    external override fun setGenre(ptr: Long, genre: String)
    external override fun setYear(ptr: Long, year: Int)
    external override fun setTrack(ptr: Long, track: Int)
    external override fun setDisc(ptr: Long, disc: Int)
    external override fun setLyrics(ptr: Long, lyrics: String)
    external override fun setCoverArt(ptr: Long, format: Int, data: ByteArray)
    external override fun deleteCoverArt(ptr: Long)
}
