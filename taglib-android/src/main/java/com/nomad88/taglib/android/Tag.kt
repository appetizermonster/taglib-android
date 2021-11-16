package com.nomad88.taglib.android

import com.nomad88.taglib.android.internal.TagNativeDelegate

class Tag internal constructor(
    internal var ptr: Long = 0L,
    private val nativeDelegate: TagNativeDelegate
) {
    fun title(): String {
        if (ptr == 0L) return ""
        return nativeDelegate.title(ptr)
    }

    fun artist(): String {
        if (ptr == 0L) return ""
        return nativeDelegate.artist(ptr)
    }

    fun album(): String {
        if (ptr == 0L) return ""
        return nativeDelegate.album(ptr)
    }

    fun albumArtist(): String {
        if (ptr == 0L) return ""
        return nativeDelegate.albumArtist(ptr)
    }

    fun genre(): String {
        if (ptr == 0L) return ""
        return nativeDelegate.genre(ptr)
    }

    fun year(): Int {
        if (ptr == 0L) return -1
        return nativeDelegate.year(ptr)
    }

    fun track(): Int {
        if (ptr == 0L) return -1
        return nativeDelegate.track(ptr)
    }

    fun disc(): Int {
        if (ptr == 0L) return -1
        return nativeDelegate.disc(ptr)
    }

    fun lyrics(): String {
        if (ptr == 0L) return ""
        return nativeDelegate.lyrics(ptr)
    }

    fun coverArtFormat(): CoverArtFormat? {
        if (ptr == 0L) return null
        return CoverArtFormat.fromNativeValue(nativeDelegate.coverArtFormat(ptr))
    }

    fun coverArtData(): ByteArray? {
        if (ptr == 0L) return null
        return nativeDelegate.coverArtData(ptr)
    }

    fun setTitle(title: String) {
        if (ptr == 0L) return
        nativeDelegate.setTitle(ptr, title)
    }

    fun setArtist(artist: String) {
        if (ptr == 0L) return
        nativeDelegate.setArtist(ptr, artist)
    }

    fun setAlbum(album: String) {
        if (ptr == 0L) return
        nativeDelegate.setAlbum(ptr, album)
    }

    fun setAlbumArtist(albumArtist: String) {
        if (ptr == 0L) return
        nativeDelegate.setAlbumArtist(ptr, albumArtist)
    }

    fun setGenre(genre: String) {
        if (ptr == 0L) return
        nativeDelegate.setGenre(ptr, genre)
    }

    fun setYear(year: Int) {
        if (ptr == 0L) return
        nativeDelegate.setYear(ptr, year)
    }

    fun setTrack(track: Int) {
        if (ptr == 0L) return
        nativeDelegate.setTrack(ptr, track)
    }

    fun setDisc(disc: Int) {
        if (ptr == 0L) return
        nativeDelegate.setDisc(ptr, disc)
    }

    fun setLyrics(lyrics: String) {
        if (ptr == 0L) return
        nativeDelegate.setLyrics(ptr, lyrics)
    }

    fun setCoverArt(format: CoverArtFormat, data: ByteArray) {
        if (ptr == 0L) return
        nativeDelegate.setCoverArt(ptr, format.nativeValue, data)
    }

    fun deleteCoverArt() {
        if (ptr == 0L) return
        nativeDelegate.deleteCoverArt(ptr)
    }

    fun save(): Boolean {
        if (ptr == 0L) return false
        return nativeDelegate.save(ptr)
    }

    fun close() {
        ptr = 0L
    }
}
