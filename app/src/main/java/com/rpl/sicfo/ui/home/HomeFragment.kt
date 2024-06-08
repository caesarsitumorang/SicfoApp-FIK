package com.rpl.sicfo.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.rpl.sicfo.R
import com.rpl.sicfo.adapter.BeritaOrganisasiAdapter
import com.rpl.sicfo.adapter.OrganisasiFikomAdapter
import com.rpl.sicfo.data.Berita
import com.rpl.sicfo.data.Organisasi
import com.rpl.sicfo.databinding.FragmentHomeBinding
import com.rpl.sicfo.ui.berita.DetailBeritaKegiatan
import com.rpl.sicfo.ui.organisasiFikom.DetailOrganisasiFikomActivity

class HomeFragment : Fragment(), BeritaOrganisasiAdapter.OnItemClickListener, OrganisasiFikomAdapter.OnItemClickListener {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var database: DatabaseReference
    private lateinit var adapterberita: BeritaOrganisasiAdapter
    private lateinit var adapterorganisasi : OrganisasiFikomAdapter
    private val beritaList = mutableListOf<Berita>()
    private val organisasiList = mutableListOf<Organisasi>()
    private lateinit var auth: FirebaseAuth

    override fun onItemClick(berita: Berita) {
        val intent = Intent(requireContext(), DetailBeritaKegiatan::class.java).apply {
            putExtra("title", berita.title)
            putExtra("imageUrl", berita.imageUrl)
            putExtra("waktu", berita.waktu)
            putExtra("information", berita.information)
        }
        startActivity(intent)
    }

    override fun onItemClick(organisasi: Organisasi) {
        val intent = Intent(requireContext(), DetailOrganisasiFikomActivity::class.java).apply {
            putExtra("title", organisasi.title)
            putExtra("logo", organisasi.logo)
            putExtra("profil", organisasi.profil)
            putExtra("image1", organisasi.image1)
            putExtra("image2", organisasi.image2)
            putExtra("image3", organisasi.image3)
            putExtra("visiMisi", organisasi.visiMisi)
            putExtra("fakultas", organisasi.fakultas)
            putExtra("strukturalImage", organisasi.strukturalImage)
            putExtra("anggota", organisasi.anggota)
            putExtra("detailTitle", organisasi.detailTitle)
        }
        startActivity(intent)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        setupRVBeritaKegiatan()
        setupRVOrganisasiFikom()
        fetchDataFromFirebase()
        fetchOrganisasiDataFromFirebase()
        fetchUserFullName()
        return binding.root
    }

    private fun setupRVOrganisasiFikom() {
        adapterorganisasi = OrganisasiFikomAdapter(organisasiList, this)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvOrganisasi.layoutManager = layoutManager
        binding.rvOrganisasi.adapter = adapterorganisasi
    }

    private fun setupRVBeritaKegiatan() {
        adapterberita = BeritaOrganisasiAdapter(beritaList, this)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvBeritaOrganisasi.layoutManager = layoutManager
        binding.rvBeritaOrganisasi.adapter = adapterberita
    }

    private fun fetchDataFromFirebase() {
        database.child("BeritaKegiatan").addValueEventListener(object : ValueEventListener {
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
                adapterberita.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
            }
        })
    }

    private fun fetchOrganisasiDataFromFirebase() {
        database.child("OrganisasiFikom").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                organisasiList.clear()
                for (dataSnapshot in snapshot.children) {
                    val title = dataSnapshot.child("title").getValue(String::class.java)
                    val logo = dataSnapshot.child("logo").getValue(String::class.java)
                    val profil = dataSnapshot.child("profil").getValue(String::class.java)
                    val fakultas = dataSnapshot.child("fakultas").getValue(String::class.java)
                    val image1 = dataSnapshot.child("image1").getValue(String::class.java)
                    val image2 = dataSnapshot.child("image2").getValue(String::class.java)
                    val image3 = dataSnapshot.child("image3").getValue(String::class.java)
                    val strukturalImage = dataSnapshot.child("strukturalImage").getValue(String::class.java)
                    val visiMisi = dataSnapshot.child("visiMisi").getValue(String::class.java)
                    val anggota = dataSnapshot.child("anggota").getValue(String::class.java)
                    val detailTitle = dataSnapshot.child("detailTitle").getValue(String::class.java)

                    if (title != null && logo != null && detailTitle != null && fakultas != null) {
                        val organisasi = Organisasi(
                            title = title,
                            logo = logo,
                            profil = profil ?: "",
                            fakultas = fakultas,
                            image1 = image1 ?: "",
                            image2 = image2 ?: "",
                            image3 = image3 ?: "",
                            strukturalImage = strukturalImage ?: "",
                            visiMisi = visiMisi ?: "",
                            anggota = anggota ?: "",
                            detailTitle = detailTitle
                        )
                        organisasiList.add(organisasi)
                    }
                }
                Log.d("HomeFragment", "Total organisasi: ${organisasiList.size}")
                adapterorganisasi.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
            }
        })
    }


    private fun fetchUserFullName() {
        val user = auth.currentUser
        if (user != null) {
            val userRef = database.child("Users").child(user.uid)
            userRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val namaLengkap = snapshot.child("namaLengkap").getValue(String::class.java)
                        val kataPertama = namaLengkap!!.split(" ")[0]
                        if (kataPertama != null) {
                            binding.tvUsername.text = kataPertama
                        } else {
                            binding.tvUsername.text = "Nama tidak ditemukan"
                        }
                    } else {
                        binding.tvUsername.text = "Pengguna tidak ditemukan"
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle database error
                    binding.tvUsername.text = "Gagal mengambil data"
                }
            })
        } else {
            binding.tvUsername.text = "Pengguna tidak login"
        }
    }
}
