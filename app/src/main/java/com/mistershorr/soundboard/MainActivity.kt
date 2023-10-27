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
    var aNote = 0
    var bbNote = 0
    var bNote = 0
    var cNote = 0
    var cSharpNote = 0
    var dNote = 0
    var dSharpNote = 0
    var eNote = 0
    var fNote = 0
    var fSharpNote = 0
    var gNote = 0
    var gSharpNote = 0
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

        aNote = soundPool.load(this, R.raw.scalea, 1)
        bbNote = soundPool.load(this, R.raw.scalebb, 1)
        bNote = soundPool.load(this, R.raw.scaleb, 1)
        cNote =  soundPool.load(this, R.raw.scalec, 1)
        cSharpNote = soundPool.load(this, R.raw.scalecs, 1)
        dNote = soundPool.load(this, R.raw.scaled, 1)
        dSharpNote = soundPool.load(this, R.raw.scaleds, 1)
        eNote = soundPool.load(this, R.raw.scalee, 1)
        fNote = soundPool.load(this, R.raw.scalef, 1)
        fSharpNote = soundPool.load(this, R.raw.scalefs, 1)
        gNote = soundPool.load(this, R.raw.scaleg, 1)
        gSharpNote = soundPool.load(this, R.raw.scalegs, 1)
        lowGNote = soundPool.load(this, R.raw.scalelowg, 1)
        highANote = soundPool.load(this, R.raw.scalehigha, 1)
        highBNote = soundPool.load(this, R.raw.scalehighb, 1)
        highBbNote = soundPool.load(this, R.raw.scalehighbb, 1)
        highCNote = soundPool.load(this, R.raw.scalehighc, 1)
        highCSharpNote = soundPool.load(this, R.raw.scalehighcs, 1)
        highDNote = soundPool.load(this, R.raw.scalehighd, 1)
        highDSharpNote = soundPool.load(this, R.raw.scalehighds, 1)
        highENote = soundPool.load(this, R.raw.scalehighe, 1)
        highFNote = soundPool.load(this, R.raw.scalehighf, 1)
        highFSharpNote = soundPool.load(this, R.raw.scalehighfs, 1)
        highGNote = soundPool.load(this, R.raw.scalehighg, 1)
        highGSharpNote = soundPool.load(this, R.raw.scalehighgs, 1)

        noteMap["A"] = aNote
        noteMap["Bb"] = bbNote
        noteMap["B"] = bNote
        noteMap["C"] = cNote
        noteMap["C#"] = cSharpNote
        noteMap["D"] = dNote
        noteMap["D#"] = dSharpNote
        noteMap["E"] = eNote
        noteMap["F"] = fNote
        noteMap["F#"] = fSharpNote
        noteMap["G"] = gNote
        noteMap["G#"] = gSharpNote
        noteMap["LG"] = lowGNote
        noteMap["HA"] = highANote
        noteMap["HB"] = highBNote
        noteMap["HBb"] = highBbNote
        noteMap["HC"] = highCNote
        noteMap["HC#"] = highCSharpNote
        noteMap["HD"] = highDNote
        noteMap["HD#"] = highDSharpNote
        noteMap["HE"] = highENote
        noteMap["HF"] = highFNote
        noteMap["HF#"] = highFSharpNote
        noteMap["HG"] = highGNote
        noteMap["HG#"] = highGSharpNote
    }

    private fun playNote(noteId: Int) {
        soundPool.play(noteId, 1f, 1f, 1, 0, 1.0f)
    }

    private inner class SoundBoardListener : View.OnClickListener {
        override fun onClick(v: View?) {
            when(v?.id) {
                R.id.button_main_a -> playNote(aNote)
                R.id.button_main_bb -> playNote(bbNote)
                R.id.button_main_b -> playNote(bNote)
                R.id.button_main_c -> playNote(cNote)
                R.id.button_main_cSharp -> playNote(cSharpNote)
                R.id.button_main_d -> playNote(dNote)
                R.id.button_main_dSharp -> playNote(dSharpNote)
                R.id.button_main_e -> playNote(eNote)
                R.id.button_main_f -> playNote(fNote)
                R.id.button_main_fSharp -> playNote(fSharpNote)
                R.id.button_main_g -> playNote(gNote)
                R.id.button_main_gSharp -> playNote(gSharpNote)
            }
        }
    }
}