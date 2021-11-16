package com.nomad88.taglib.android

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MP4FileTest {

    private val ctx: Context = ApplicationProvider.getApplicationContext()

    @get:Rule
    val tempFolder = TemporaryFolder(ctx.cacheDir)

    @Test
    fun isSupportedReturnsTrueForMP4Files() {
        val file = ctx.inflateAsset(tempFolder.root, "test.m4a")
        Assert.assertTrue(MP4File.isSupported(file.absolutePath))
    }

    @Test
    fun isSupportedReturnsFalseForNonMP4Files() {
        val file = ctx.inflateAsset(tempFolder.root, "test.ogg")
        Assert.assertFalse(MP4File.isSupported(file.absolutePath))
    }

    @Test
    fun testReadTag() {
        val file = ctx.inflateAsset(tempFolder.root, "test_with_tags.m4a")
        MP4File.create(file.absolutePath, readAudioProperties = false)!!.use {
            val tag = it.tag()!!
            Assert.assertEquals("Hello, world", tag.title())
            Assert.assertEquals("Artist name", tag.artist())
            Assert.assertEquals("앨범", tag.album())
            Assert.assertEquals("앨범 아티스트", tag.albumArtist())
            Assert.assertEquals("힙합", tag.genre())
            Assert.assertEquals(1515, tag.year())
            Assert.assertEquals(15, tag.track())
            Assert.assertEquals(10, tag.disc())
            Assert.assertEquals("Hello, world\n안녕하세요.", tag.lyrics())
            Assert.assertNull(tag.coverArtFormat())
            Assert.assertNull(tag.coverArtData())
        }
    }

    @Test
    fun testReadCoverArt() {
        val file = ctx.inflateAsset(tempFolder.root, "test_with_album_art_m4a.mp3")
        MP4File.create(file.absolutePath, readAudioProperties = false)!!.use {
            val tag = it.tag()!!
            Assert.assertEquals(CoverArtFormat.JPEG, tag.coverArtFormat())

            val coverArtData = tag.coverArtData()
            Assert.assertNotNull(coverArtData)
            Assert.assertTrue(coverArtData!!.isNotEmpty())
        }
    }

    @Test
    fun testAudioProperties() {
        val file = ctx.inflateAsset(tempFolder.root, "test.m4a")
        MP4File.create(file.absolutePath, readAudioProperties = true)!!.use {
            val audioProps = it.audioProperties()!!
            Assert.assertEquals(44100, audioProps.sampleRate())
            Assert.assertEquals(131, audioProps.bitrate())
        }
    }

    @Test
    fun testEditTag() {
        val file = ctx.inflateAsset(tempFolder.root, "test.m4a")
        MP4File.create(file.absolutePath, readAudioProperties = false)!!.use {
            val tag = it.tag()!!
            tag.setTitle("Hello, world")
            tag.setArtist("Artist name")
            tag.setAlbum("앨범")
            tag.setAlbumArtist("앨범 아티스트")
            tag.setGenre("힙합")
            tag.setYear(1515)
            tag.setTrack(9)
            tag.setDisc(10)
            tag.setLyrics("Hello, world\n안녕하세요.")
            tag.deleteCoverArt()
            it.save()
        }
        MP4File.create(file.absolutePath, readAudioProperties = false)!!.use {
            val tag = it.tag()!!
            Assert.assertEquals("Hello, world", tag.title())
            Assert.assertEquals("Artist name", tag.artist())
            Assert.assertEquals("앨범", tag.album())
            Assert.assertEquals("앨범 아티스트", tag.albumArtist())
            Assert.assertEquals("힙합", tag.genre())
            Assert.assertEquals(1515, tag.year())
            Assert.assertEquals(9, tag.track())
            Assert.assertEquals(10, tag.disc())
            Assert.assertEquals("Hello, world\n안녕하세요.", tag.lyrics())
            Assert.assertNull(tag.coverArtFormat())
            Assert.assertNull(tag.coverArtData())
        }
    }

    @Test
    fun testEditCoverArt() {
        val file = ctx.inflateAsset(tempFolder.root, "test.m4a")
        val albumArtBytes = ctx.readAssetBytes("album_art.jpg")
        MP4File.create(file.absolutePath, readAudioProperties = false)!!.use {
            val tag = it.tag()!!
            tag.setCoverArt(CoverArtFormat.JPEG, albumArtBytes)
            it.save()
        }
        MP4File.create(file.absolutePath, readAudioProperties = false)!!.use {
            val tag = it.tag()!!
            Assert.assertEquals(CoverArtFormat.JPEG, tag.coverArtFormat())
            Assert.assertTrue(albumArtBytes.contentEquals(tag.coverArtData()))
        }
    }
}
