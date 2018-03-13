package com.werureo.orlandocodecamp.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.werureo.orlandocodecamp.R
import com.werureo.orlandocodecamp.adapters.SessionsAdapter
import com.werureo.orlandocodecamp.models.ScheduleSection
import com.werureo.orlandocodecamp.models.Session
import com.werureo.orlandocodecamp.models.Track
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val db = FirebaseFirestore.getInstance()
    var scheduleSections = mutableListOf<ScheduleSection>()
    lateinit var sessionsAdapter: SessionsAdapter

    @SuppressLint("LogNotTimber")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sessionsAdapter = SessionsAdapter(this, scheduleSections)

        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.setHasFixedSize(true)
        recyclerview.adapter = sessionsAdapter

        db.collection("timeslots")
                .orderBy("rank")
                .get()
                .addOnSuccessListener {
                    for (timeslot in it) {
                        val timeslotId = timeslot.id
                        val sessions = mutableListOf<Session>()
                        db.collection("sessions")
                                .get()
                                .addOnSuccessListener {
                                    for (session in it) {
                                        if (session.get("timeslot") == timeslotId) {
                                            val track = Track(
                                                    name = session.get("track.name") as String,
                                                    roomNumber = session.get("track.roomNumber") as String
                                            )
                                            sessions.add(
                                                    Session(
                                                            name = session.get("name") as String,
                                                            description = session.get("description") as String,
                                                            level = session.get("level") as String,
                                                            speaker = session.get("speaker.fullName") as String,
                                                            speakerImageUrl = session.get("speaker.imageUrl") as String,
                                                            track = track,
                                                            timeslot = session.get("timeslot") as String
                                                    )
                                            )
                                        }
                                    }

                                    scheduleSections.add(
                                            ScheduleSection(
                                                    timeslot = timeslot.get("time") as String,
                                                    sessions = sessions
                                            ))

                                    sessionsAdapter.notifyDataSetChanged()
                                }
                                .addOnFailureListener {
                                    Log.e("test", "Could not retrieve sessions: $it")
                                }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("test", "Could not retrieve timeslots: $exception")
                }
    }
}
