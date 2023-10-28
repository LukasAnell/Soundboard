package com.mistershorr.soundboard

import android.media.AudioManager
import android.media.SoundPool
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.constraintlayout.widget.Group
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mistershorr.soundboard.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var buttonA: Button
    lateinit var buttonBb: Button
    lateinit var buttonB: Button
    lateinit var buttonC: Button
    lateinit var buttonCSharp: Button
    lateinit var buttonD: Button
    lateinit var buttonDSharp: Button
    lateinit var buttonE: Button
    lateinit var buttonF: Button
    lateinit var buttonFSharp: Button
    lateinit var buttonG: Button
    lateinit var buttonGSharp: Button
    lateinit var buttonPlaySong: Button
    lateinit var groupMainNoteButtons: Group
    lateinit var soundPool: SoundPool

//    var A0 = 0
//    var A1 = 0
//    var A2 = 0
    var A3 = 0
//    var A4 = 0
//    var A5 = 0
//    var A7 = 0
//    var B1 = 0
//    var B2 = 0
    var B3 = 0
//    var B4 = 0
//    var B5 = 0
//    var B7 = 0
//    var Bb1 = 0
//    var Bb2 = 0
    var Bb3 = 0
//    var Bb4 = 0
//    var Bb5 = 0
//    var Bb7 = 0
//    var C1 = 0
//    var C2 = 0
//    var C3 = 0
    var C4 = 0
//    var C5 = 0
//    var C6 = 0
//    var C8 = 0
//    var CSharp2 = 0
//    var CSharp3 = 0
    var CSharp4 = 0
//    var CSharp5 = 0
//    var D1 = 0
//    var D2 = 0
//    var D3 = 0
    var D4 = 0
//    var D5 = 0
//    var DSharp1 = 0
//    var DSharp2 = 0
//    var DSharp3 = 0
    var DSharp4 = 0
//    var DSharp5 = 0
//    var E1 = 0
//    var E2 = 0
//    var E3 = 0
    var E4 = 0
//    var E5 = 0
//    var F1 = 0
//    var F2 = 0
//    var F3 = 0
    var F4 = 0
//    var F5 = 0
//    var FSharp0 = 0
//    var FSharp1 = 0
//    var FSharp2 = 0
//    var FSharp3 = 0
    var FSharp4 = 0
//    var FSharp5 = 0
//    var G0 = 0
//    var G1 = 0
//    var G2 = 0
//    var G3 = 0
    var G4 = 0
//    var G5 = 0
//    var GSharp0 = 0
//    var GSharp1 = 0
//    var GSharp2 = 0
//    var GSharp3 = 0
    var GSharp4 = 0
//    var GSharp5 = 0

    lateinit var binding: ActivityMainBinding

    lateinit var noteList: ArrayList<Note>
    var separatedNoteList: ArrayList<Note> = ArrayList()
    var noteMap = HashMap<String, Int>()

    companion object {
        const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        wireWidgets()
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
        val sType = object: TypeToken<List<Note>>() { }.type
        noteList = gson.fromJson(jsonString, sType)
        Log.d(TAG, "$noteList")
    }

    private fun setListeners() {
        val soundBoardListener = SoundBoardListener()
        buttonA.setOnClickListener(soundBoardListener)
        buttonBb.setOnClickListener(soundBoardListener)
        buttonB.setOnClickListener(soundBoardListener)
        buttonC.setOnClickListener(soundBoardListener)
        buttonCSharp.setOnClickListener(soundBoardListener)
        buttonD.setOnClickListener(soundBoardListener)
        buttonDSharp.setOnClickListener(soundBoardListener)
        buttonE.setOnClickListener(soundBoardListener)
        buttonF.setOnClickListener(soundBoardListener)
        buttonFSharp.setOnClickListener(soundBoardListener)
        buttonG.setOnClickListener(soundBoardListener)
        buttonGSharp.setOnClickListener(soundBoardListener)
        buttonPlaySong.setOnClickListener {
            GlobalScope.launch {
                playSong(noteList)
            }
        }
    }


    private fun wireWidgets() {
        buttonA = findViewById(R.id.button_main_a)
        buttonBb = findViewById(R.id.button_main_bb)
        buttonB = findViewById(R.id.button_main_b)
        buttonC = findViewById(R.id.button_main_c)
        buttonCSharp = findViewById(R.id.button_main_cSharp)
        buttonD = findViewById(R.id.button_main_d)
        buttonDSharp = findViewById(R.id.button_main_dSharp)
        buttonE = findViewById(R.id.button_main_e)
        buttonF = findViewById(R.id.button_main_f)
        buttonFSharp = findViewById(R.id.button_main_fSharp)
        buttonG = findViewById(R.id.button_main_g)
        buttonGSharp = findViewById(R.id.button_main_gSharp)
        buttonPlaySong = findViewById(R.id.button_main_playSong)
        groupMainNoteButtons = findViewById(R.id.group_main_noteButtons)
    }

    private fun initializeSoundPool() {
        this.volumeControlStream = AudioManager.STREAM_MUSIC
        soundPool = SoundPool(10, AudioManager.STREAM_MUSIC, 0)
//        soundPool.setOnLoadCompleteListener(SoundPool.OnLoadCompleteListener { soundPool, sampleId, status ->
//           // isSoundPoolLoaded = true
//        })

        noteMap["A0"] = soundPool.load(this, R.raw.a0, 1)
        noteMap["A1"] = soundPool.load(this, R.raw.a1, 1)
        noteMap["A2"] = soundPool.load(this, R.raw.a2, 1)
        noteMap["A3"] = soundPool.load(this, R.raw.a3, 1)
        noteMap["A4"] = soundPool.load(this, R.raw.a4, 1)
        noteMap["A5"] = soundPool.load(this, R.raw.a5, 1)
        noteMap["A7"] = soundPool.load(this, R.raw.a7, 1)
        noteMap["B1"] = soundPool.load(this, R.raw.b1, 1)
        noteMap["B2"] = soundPool.load(this, R.raw.b2, 1)
        noteMap["B3"] = soundPool.load(this, R.raw.b3, 1)
        noteMap["B4"] = soundPool.load(this, R.raw.b4, 1)
        noteMap["B5"] = soundPool.load(this, R.raw.b5, 1)
        noteMap["B7"] = soundPool.load(this, R.raw.b7, 1)
        noteMap["Bb1"] = soundPool.load(this, R.raw.bb1, 1)
        noteMap["Bb2"] = soundPool.load(this, R.raw.bb2, 1)
        noteMap["Bb3"] = soundPool.load(this, R.raw.b3, 1)
        noteMap["Bb4"] = soundPool.load(this, R.raw.bb4, 1)
        noteMap["Bb5"] = soundPool.load(this, R.raw.bb5, 1)
        noteMap["Bb7"] = soundPool.load(this, R.raw.bb7, 1)
        noteMap["C1"] = soundPool.load(this, R.raw.c1, 1)
        noteMap["C2"] = soundPool.load(this, R.raw.c2, 1)
        noteMap["C3"] = soundPool.load(this, R.raw.c3, 1)
        noteMap["C4"] = soundPool.load(this, R.raw.c4, 1)
        noteMap["C5"] = soundPool.load(this, R.raw.c5, 1)
        noteMap["C6"] = soundPool.load(this, R.raw.c6, 1)
        noteMap["C8"] = soundPool.load(this, R.raw.c8, 1)
        noteMap["CSharp2"] = soundPool.load(this, R.raw.csharp2, 1)
        noteMap["CSharp3"] = soundPool.load(this, R.raw.csharp3, 1)
        noteMap["CSharp4"] = soundPool.load(this, R.raw.csharp4, 1)
        noteMap["CSharp5"] = soundPool.load(this, R.raw.csharp5, 1)
        noteMap["D1"] = soundPool.load(this, R.raw.d1, 1)
        noteMap["D2"] = soundPool.load(this, R.raw.d2, 1)
        noteMap["D3"] = soundPool.load(this, R.raw.d3, 1)
        noteMap["D4"] = soundPool.load(this, R.raw.d4, 1)
        noteMap["D5"] = soundPool.load(this, R.raw.d5, 1)
        noteMap["DSharp1"] = soundPool.load(this, R.raw.dsharp1, 1)
        noteMap["DSharp2"] = soundPool.load(this, R.raw.dsharp2, 1)
        noteMap["DSharp3"] = soundPool.load(this, R.raw.dsharp3, 1)
        noteMap["DSharp4"] = soundPool.load(this, R.raw.dsharp4, 1)
        noteMap["DSharp5"] = soundPool.load(this, R.raw.dsharp5, 1)
        noteMap["E1"] = soundPool.load(this, R.raw.f1, 1)
        noteMap["E2"] = soundPool.load(this, R.raw.f2, 1)
        noteMap["E3"] = soundPool.load(this, R.raw.f3, 1)
        noteMap["E4"] = soundPool.load(this, R.raw.f4, 1)
        noteMap["E5"] = soundPool.load(this, R.raw.f5, 1)
        noteMap["F1"] = soundPool.load(this, R.raw.f1, 1)
        noteMap["F2"] = soundPool.load(this, R.raw.f2, 1)
        noteMap["F3"] = soundPool.load(this, R.raw.f3, 1)
        noteMap["F4"] = soundPool.load(this, R.raw.f4, 1)
        noteMap["F5"] = soundPool.load(this, R.raw.f5, 1)
        noteMap["FSharp0"] = soundPool.load(this, R.raw.fsharp0, 1)
        noteMap["FSharp1"] = soundPool.load(this, R.raw.fsharp1, 1)
        noteMap["FSharp2"] = soundPool.load(this, R.raw.fsharp2, 1)
        noteMap["FSharp3"] = soundPool.load(this, R.raw.fsharp3, 1)
        noteMap["FSharp4"] = soundPool.load(this, R.raw.fsharp4, 1)
        noteMap["FSharp5"] = soundPool.load(this, R.raw.fsharp5, 1)
        noteMap["G0"] = soundPool.load(this, R.raw.g0, 1)
        noteMap["G1"] = soundPool.load(this, R.raw.g1, 1)
        noteMap["G2"] = soundPool.load(this, R.raw.g2, 1)
        noteMap["G3"] = soundPool.load(this, R.raw.g3, 1)
        noteMap["G4"] = soundPool.load(this, R.raw.g4, 1)
        noteMap["G5"] = soundPool.load(this, R.raw.g5, 1)
        noteMap["GSharp0"] = soundPool.load(this, R.raw.gsharp0, 1)
        noteMap["GSharp1"] = soundPool.load(this, R.raw.gsharp1, 1)
        noteMap["GSharp2"] = soundPool.load(this, R.raw.gsharp2, 1)
        noteMap["GSharp3"] = soundPool.load(this, R.raw.gsharp3, 1)
        noteMap["GSharp4"] = soundPool.load(this, R.raw.gsharp4, 1)
        noteMap["GSharp5"] = soundPool.load(this, R.raw.gsharp5, 1)
    }

    private fun playNote(noteId: Int) {
        soundPool.play(noteId, 1f, 1f, 1, 0, 1.0f)
    }

    private inner class SoundBoardListener : View.OnClickListener {
        override fun onClick(v: View?) {
            when(v?.id) {
                R.id.button_main_a -> playNote(A3)
                R.id.button_main_bb -> playNote(Bb3)
                R.id.button_main_b -> playNote(B3)
                R.id.button_main_c -> playNote(C4)
                R.id.button_main_cSharp -> playNote(CSharp4)
                R.id.button_main_d -> playNote(D4)
                R.id.button_main_dSharp -> playNote(DSharp4)
                R.id.button_main_e -> playNote(E4)
                R.id.button_main_f -> playNote(F4)
                R.id.button_main_fSharp -> playNote(FSharp4)
                R.id.button_main_g -> playNote(G4)
                R.id.button_main_gSharp -> playNote(GSharp4)
            }
        }
    }
}