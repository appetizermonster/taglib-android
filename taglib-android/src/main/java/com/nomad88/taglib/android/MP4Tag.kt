package com.nomad88.taglib.android

class MP4Tag internal constructor(internal var ptr: Long) : AutoCloseable {

    fun title(): String {
        if (ptr == 0L) return ""
        return TagLib.mp4Tag_title(ptr)
    }

    fun artist(): String {
        if (ptr == 0L) return ""
        return TagLib.mp4Tag_artist(ptr)
    }

    fun album(): String {
        if (ptr == 0L) return ""
        return TagLib.mp4Tag_album(ptr)
    }

    fun albumArtist(): String {
        if (ptr == 0L) return ""
        return TagLib.mp4Tag_albumArtist(ptr)
    }

    fun genre(): String {
        if (ptr == 0L) return ""
        return TagLib.mp4Tag_genre(ptr)
    }

    fun year(): Int {
        if (ptr == 0L) return -1
        return TagLib.mp4Tag_year(ptr)
    }

    fun track(): Int {
        if (ptr == 0L) return -1
        return TagLib.mp4Tag_track(ptr)
    }

    fun disc(): Int {
        if (ptr == 0L) return -1
        return TagLib.mp4Tag_disc(ptr)
    }

    fun lyrics(): String {
        if (ptr == 0L) return ""
        return TagLib.mp4Tag_lyrics(ptr)
    }

    fun setTitle(title: String) {
        if (ptr == 0L) return
        TagLib.mp4Tag_setTitle(ptr, title)
    }

    fun setArtist(artist: String) {
        if (ptr == 0L) return
        TagLib.mp4Tag_setArtist(ptr, artist)
    }

    fun setAlbum(album: String) {
        if (ptr == 0L) return
        TagLib.mp4Tag_setAlbum(ptr, album)
    }

    fun setAlbumArtist(albumArtist: String) {
        if (ptr == 0L) return
        TagLib.mp4Tag_setAlbumArtist(ptr, albumArtist)
    }

    fun setGenre(genre: String) {
        if (ptr == 0L) return
        TagLib.mp4Tag_setGenre(ptr, genre)
    }

    fun setYear(year: Int) {
        if (ptr == 0L) return
        TagLib.mp4Tag_setYear(ptr, year)
    }

    fun setTrack(track: Int) {
        if (ptr == 0L) return
        TagLib.mp4Tag_setTrack(ptr, track)
    }

    fun setDisc(disc: Int) {
        if (ptr == 0L) return
        TagLib.mp4Tag_setDisc(ptr, disc)
    }

    fun setLyrics(lyrics: String) {
        if (ptr == 0L) return
        TagLib.mp4Tag_setLyrics(ptr, lyrics)
    }

    fun save(): Boolean {
        if (ptr == 0L) return false
        return TagLib.mp4Tag_save(ptr)
    }

    override fun close() {
        ptr = 0L
    }
}
