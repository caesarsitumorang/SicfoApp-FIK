package com.rpl.sicfo.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.rpl.sicfo.R
import com.rpl.sicfo.adapter.BeritaOrganisasiAdapter
import com.rpl.sicfo.data.Berita
import com.rpl.sicfo.data.User
import com.rpl.sicfo.databinding.FragmentHomeBinding
import com.rpl.sicfo.ui.berita.DetailBeritaKegiatan

class HomeFragment : Fragment(), BeritaOrganisasiAdapter.OnItemClickListener {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var database: DatabaseReference
    private lateinit var adapter: BeritaOrganisasiAdapter
    private val beritaList = mutableListOf<Berita>()

    override fun onItemClick(berita: Berita) {
        val intent = Intent(requireContext(), DetailBeritaKegiatan::class.java).apply {
            putExtra("title", berita.title)
            putExtra("imageUrl", berita.imageUrl)
            putExtra("waktu", berita.waktu)
            putExtra("information", berita.information)
        }
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = FirebaseDatabase.getInstance().getReference("BeritaKegiatan")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        setupRecyclerView()
        fetchDataFromFirebase()
        fetchUserFullName()  // Fetch user full name
        return binding.root
    }

    private fun setupRecyclerView() {
        adapter = BeritaOrganisasiAdapter(beritaList, this)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvBeritaOrganisasi.layoutManager = layoutManager
        binding.rvBeritaOrganisasi.adapter = adapter
    }

    private fun fetchDataFromFirebase() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                beritaList.clear()
                for (dataSnapshot in snapshot.children) {
                    val title = dataSnapshot.child("title").getValue(String::class.java)
                    val imageUrl = dataSnapshot.child("image").getValue(String::class.java)
                    val waktu = dataSnapshot.child("waktu").getValue(String::class.java)
                    val information = dataSnapshot.child("information").getValue(String::class.java)
                    if (title != null && imageUrl != null && waktu != null && information != null) {
                        val berita = Berita(title, imageUrl, waktu, information)
                        beritaList.add(berita)
                    }
                }
                adapter.notifyDataSetChanged()
            }


            override fun onCancelled(error: DatabaseError) {
                // Handle database error
            }
        })
    }

    private fun fetchUserFullName() {
        val userId = "user_id"  // Replace this with the actual user ID logic
        val userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId)
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                if (user != null) {
                    binding.tvUsername.text = user.fullName
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
            }
        })
    }
}
