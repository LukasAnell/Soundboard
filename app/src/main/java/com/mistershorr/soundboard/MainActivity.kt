package com.mistershorr.soundboard

import android.media.AudioManager
import android.media.SoundPool
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mistershorr.soundboard.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var soundPool: SoundPool
    var aZero = 0
    var aOne = 0
    var aTwo = 0
    var aThree = 0
    var aFour = 0
    var aFive = 0
    var aSeven = 0

    var bbOne = 0
    var bbTwo = 0
    var bbThree = 0
    var bbFour = 0
    var bbFive = 0
    var bbSeven = 0

    var bOne = 0
    var bTwo = 0
    var bThree = 0
    var bFour = 0
    var bFive = 0
    var bSeven = 0

    var cOne = 0
    var cTwo = 0
    var cThree = 0
    var cFour = 0
    var cFive = 0
    var cEight = 0

    var cSharpTwo = 0
    var cSharpThree = 0
    var cSharpFour = 0
    var cSharpFive = 0

    var dOne = 0
    var dTwo = 0
    var dThree = 0
    var dFour = 0
    var dFive = 0

    var dSharpOne = 0
    var dSharpTwo = 0
    var dSharpThree = 0
    var dSharpFour = 0
    var dSharpFive = 0

    var eOne = 0
    var eTwo = 0
    var eThree = 0
    var eFour = 0
    var eFive = 0

    var fOne = 0
    var fTwo = 0
    var fThree = 0
    var fFour = 0
    var fFive = 0

    var fSharpOne = 0
    var fSharpTwo = 0
    var fSharpThree = 0
    var fSharpFour = 0
    var fSharpFive = 0

    var gOne = 0
    var gTwo = 0
    var gThree = 0
    var gFour = 0
    var gFive = 0

    var gSharpOne = 0
    var gSharpTwo = 0
    var gSharpThree = 0
    var gSharpFour = 0
    var gSharpFive = 0

    var lowGNote = 0
    var highANote = 0
    var highBNote = 0
    var highBbNote = 0
    var highCNote = 0
    var highCSharpNote = 0
    var highDNote = 0
    var highDSharpNote = 0
    var highENote = 0
    var highFNote = 0
    var highFSharpNote = 0
    var highGNote = 0
    var highGSharpNote = 0

    private lateinit var binding: ActivityMainBinding

    lateinit var noteList: ArrayList<Chord>
    var separatedNoteList: ArrayList<Note> = ArrayList()
    var noteMap = HashMap<String, Int>()

    companion object {
        const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeSoundPool()
        setListeners()
        readJSON()
        // Log.d(TAG, "onCreate: $separatedNoteList")
        Thread.sleep(5000)
        // playSong(noteList)
    }

    private fun playSong(song: List<Note>) {
        GlobalScope.launch(Dispatchers.Main) {
            binding.groupMainNoteButtons.referencedIds.forEach {
                findViewById<Button>(it).isEnabled = false
            }
        }
        for(note in song) {
            playNote(noteMap[note.note] ?: 0)
            delay(note.duration.toLong() / 3)
        }
        GlobalScope.launch(Dispatchers.Main) {
            binding.groupMainNoteButtons.referencedIds.forEach {
                findViewById<Button>(it).isEnabled = true
            }
        }
    }

    private fun playSongWithChords(song: ArrayList<Chord>) {
        GlobalScope.launch(Dispatchers.Main) {
            binding.groupMainNoteButtons.referencedIds.forEach {
                findViewById<Button>(it).isEnabled = false
            }
        }
        for(chord in song) {
            for (note in chord.chord) {
                playNote(noteMap[note.note] ?: 0)
                Log.d(TAG, "playSongWithChords: ${note.note}")
            }
            delay(chord.duration.toLong())
        }
        GlobalScope.launch(Dispatchers.Main) {
            binding.groupMainNoteButtons.referencedIds.forEach {
                findViewById<Button>(it).isEnabled = true
            }
        }
    }

    private fun playNote(note: String) {
        // ?: is the elvis operator. it lets you provide a default value
        // if the value is null
        playNote(noteMap[note] ?: 0)
    }

    private fun delay(time: Long) {
        try {
            Thread.sleep(time)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    private fun convertStringToList(notesAndDurations: String) {
        val temp = notesAndDurations.split(" ")
        for(i in temp.indices step 2) {
            separatedNoteList.add(Note(temp[i], temp[i+1].toInt()))
        }
    }


    private fun readJSON() {
        val inputStream = resources.openRawResource(R.raw.song)
        val jsonString = inputStream.bufferedReader().use {
            it.readText()
        }
        Log.d(TAG, "onCreate: jsonString $jsonString")
        val gson = Gson()
        val sType = object: TypeToken<List<Chord>>() { }.type
        noteList = gson.fromJson(jsonString, sType)
        Log.d(TAG, "$noteList")
    }

    private fun setListeners() {
        val soundBoardListener = SoundBoardListener()
        binding.buttonMainA.setOnClickListener(soundBoardListener)
        binding.buttonMainBb.setOnClickListener(soundBoardListener)
        binding.buttonMainB.setOnClickListener(soundBoardListener)
        binding.buttonMainC.setOnClickListener(soundBoardListener)
        binding.buttonMainCSharp.setOnClickListener(soundBoardListener)
        binding.buttonMainD.setOnClickListener(soundBoardListener)
        binding.buttonMainDSharp.setOnClickListener(soundBoardListener)
        binding.buttonMainE.setOnClickListener(soundBoardListener)
        binding.buttonMainF.setOnClickListener(soundBoardListener)
        binding.buttonMainFSharp.setOnClickListener(soundBoardListener)
        binding.buttonMainG.setOnClickListener(soundBoardListener)
        binding.buttonMainGSharp.setOnClickListener(soundBoardListener)
        binding.buttonMainPlaySong.setOnClickListener {
            GlobalScope.launch {
                playSongWithChords(noteList)
            }
        }
    }

    private fun initializeSoundPool() {
        this.volumeControlStream = AudioManager.STREAM_MUSIC
        soundPool = SoundPool(10, AudioManager.STREAM_MUSIC, 0)
//        soundPool.setOnLoadCompleteListener(SoundPool.OnLoadCompleteListener { soundPool, sampleId, status ->
//           // isSoundPoolLoaded = true
//        })

        aZero = soundPool.load(this, R.raw.azero, 1)
        aOne = soundPool.load(this, R.raw.aone, 1)
        aTwo = soundPool.load(this, R.raw.atwo, 1)
        aThree = soundPool.load(this, R.raw.athree, 1)
        aFour = soundPool.load(this, R.raw.afour, 1)
        aFive = soundPool.load(this, R.raw.afive, 1)
        aSeven = soundPool.load(this, R.raw.aseven, 1)

        bbOne = soundPool.load(this, R.raw.bbone, 1)
        bbTwo = soundPool.load(this, R.raw.bbtwo, 1)
        bbThree = soundPool.load(this, R.raw.bbthree, 1)
        bbFour = soundPool.load(this, R.raw.bbfour, 1)
        bbFive = soundPool.load(this, R.raw.bbfive, 1)
        bbSeven = soundPool.load(this, R.raw.bbseven, 1)

        bOne = soundPool.load(this, R.raw.bone, 1)
        bTwo = soundPool.load(this, R.raw.btwo, 1)
        bThree = soundPool.load(this, R.raw.bthree, 1)
        bFour = soundPool.load(this, R.raw.bfour, 1)
        bFive = soundPool.load(this, R.raw.bfive, 1)
        bSeven = soundPool.load(this, R.raw.bseven, 1)

        cOne = soundPool.load(this, R.raw.cone, 1)
        cTwo = soundPool.load(this, R.raw.ctwo, 1)
        cThree = soundPool.load(this, R.raw.cthree, 1)
        cFour = soundPool.load(this, R.raw.cfour, 1)
        cFive = soundPool.load(this, R.raw.cfive, 1)
        cEight = soundPool.load(this, R.raw.ceight, 1)

        cSharpTwo = soundPool.load(this, R.raw.cstwo, 1)
        cSharpThree = soundPool.load(this, R.raw.csthree, 1)
        cSharpFour = soundPool.load(this, R.raw.csfour, 1)
        cSharpFive = soundPool.load(this, R.raw.csfive, 1)

        dOne = soundPool.load(this, R.raw.done, 1)
        dTwo = soundPool.load(this, R.raw.dtwo, 1)
        dThree = soundPool.load(this, R.raw.dthree, 1)
        dFour = soundPool.load(this, R.raw.dfour, 1)
        dFive = soundPool.load(this, R.raw.dfive, 1)

        dSharpOne = soundPool.load(this, R.raw.dsone, 1)
        dSharpTwo = soundPool.load(this, R.raw.dstwo, 1)
        dSharpThree = soundPool.load(this, R.raw.dsthree, 1)
        dSharpFour = soundPool.load(this, R.raw.dsfour, 1)
        dSharpFive = soundPool.load(this, R.raw.dsfive, 1)

        eOne = soundPool.load(this, R.raw.eone, 1)
        eTwo = soundPool.load(this, R.raw.etwo, 1)
        eThree = soundPool.load(this, R.raw.ethree, 1)
        eFour = soundPool.load(this, R.raw.efour, 1)
        eFive = soundPool.load(this, R.raw.efive, 1)

        fOne = soundPool.load(this, R.raw.fone, 1)
        fTwo = soundPool.load(this, R.raw.ftwo, 1)
        fThree = soundPool.load(this, R.raw.fthree, 1)
        fFour = soundPool.load(this, R.raw.ffour, 1)
        fFive = soundPool.load(this, R.raw.ffive, 1)

        fSharpOne = soundPool.load(this, R.raw.fsone, 1)
        fSharpTwo = soundPool.load(this, R.raw.fstwo, 1)
        fSharpThree = soundPool.load(this, R.raw.fsthree, 1)
        fSharpFour = soundPool.load(this, R.raw.fsfour, 1)
        fSharpFive = soundPool.load(this, R.raw.fsfive, 1)

        gOne = soundPool.load(this, R.raw.gone, 1)
        gTwo = soundPool.load(this, R.raw.gtwo, 1)
        gThree = soundPool.load(this, R.raw.gthree, 1)
        gFour = soundPool.load(this, R.raw.gfour, 1)
        gFive = soundPool.load(this, R.raw.gfive, 1)

        gSharpOne = soundPool.load(this, R.raw.gsone, 1)
        gSharpTwo = soundPool.load(this, R.raw.gstwo, 1)
        gSharpThree = soundPool.load(this, R.raw.gsthree, 1)
        gSharpFour = soundPool.load(this, R.raw.gsfour, 1)
        gSharpFive = soundPool.load(this, R.raw.gsfive, 1)

        noteMap["A0"] = aZero
        noteMap["A1"] = aOne
        noteMap["A2"] = aTwo
        noteMap["A3"] = aThree
        noteMap["A4"] = aFour
        noteMap["A5"] = aFive
        noteMap["A7"] = aSeven

        noteMap["Bb1"] = bbOne
        noteMap["Bb2"] = bbTwo
        noteMap["Bb3"] = bbThree
        noteMap["Bb4"] = bbFour
        noteMap["Bb5"] = bbFive
        noteMap["Bb7"] = bbSeven

        noteMap["B1"] = bOne
        noteMap["B2"] = bTwo
        noteMap["B3"] = bThree
        noteMap["B4"] = bFour
        noteMap["B5"] = bFive
        noteMap["B7"] = bSeven

        noteMap["C1"] = cOne
        noteMap["C2"] = cTwo
        noteMap["C3"] = cThree
        noteMap["C4"] = cFour
        noteMap["C5"] = cFive
        noteMap["C8"] = cEight

        noteMap["C#2"] = cSharpTwo
        noteMap["C#3"] = cSharpThree
        noteMap["C#4"] = cSharpFour
        noteMap["C#5"] = cSharpFive

        noteMap["D1"] = dOne
        noteMap["D2"] = dTwo
        noteMap["D3"] = dThree
        noteMap["D4"] = dFour
        noteMap["D5"] = dFive

        noteMap["D#1"] = dSharpOne
        noteMap["D#2"] = dSharpTwo
        noteMap["D#3"] = dSharpThree
        noteMap["D#4"] = dSharpFour
        noteMap["D#5"] = dSharpFive

        noteMap["E1"] = eOne
        noteMap["E2"] = eTwo
        noteMap["E3"] = eThree
        noteMap["E4"] = eFour
        noteMap["E5"] = eFive

        noteMap["F1"] = fOne
        noteMap["F2"] = fTwo
        noteMap["F3"] = fThree
        noteMap["F4"] = fFour
        noteMap["F5"] = fFive

        noteMap["F#1"] = fSharpOne
        noteMap["F#2"] = fSharpTwo
        noteMap["F#3"] = fSharpThree
        noteMap["F#4"] = fSharpFour
        noteMap["F#5"] = fSharpFive

        noteMap["G1"] = gOne
        noteMap["G2"] = gTwo
        noteMap["G3"] = gThree
        noteMap["G4"] = gFour
        noteMap["G5"] = gFive

        noteMap["G#1"] = gSharpOne
        noteMap["G#2"] = gSharpTwo
        noteMap["G#3"] = gSharpThree
        noteMap["G#4"] = gSharpFour
        noteMap["G#5"] = gSharpFive
    }

    private fun playNote(noteId: Int) {
        soundPool.play(noteId, 1f, 1f, 1, 0, 1.0f)
    }

    private inner class SoundBoardListener : View.OnClickListener {
        override fun onClick(v: View?) {
            when(v?.id) {
                R.id.button_main_a -> playNote(aThree)
                R.id.button_main_bb -> playNote(bbThree)
                R.id.button_main_b -> playNote(bThree)
                R.id.button_main_c -> playNote(cFour)
                R.id.button_main_cSharp -> playNote(cSharpFour)
                R.id.button_main_d -> playNote(dFour)
                R.id.button_main_dSharp -> playNote(dSharpFour)
                R.id.button_main_e -> playNote(eFour)
                R.id.button_main_f -> playNote(fFour)
                R.id.button_main_fSharp -> playNote(fSharpFour)
                R.id.button_main_g -> playNote(gFour)
                R.id.button_main_gSharp -> playNote(gSharpFour)
            }
        }
    }
}