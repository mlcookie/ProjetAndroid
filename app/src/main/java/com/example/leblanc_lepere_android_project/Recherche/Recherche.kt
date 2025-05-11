package com.example.leblanc_lepere_android_project.Recherche

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.leblanc_lepere_android_project.R


class Recherche : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var btnFilters: Button
    private lateinit var btnSwitchView: Button
    private lateinit var adapter: ResultAdapter
    private lateinit var cardView: LinearLayout
    private var isListView = true

    //Liste des résultats:
    private var itemList = listOf(
        Petsitter("Petsitter 1", "https://i.pravatar.cc/150?img=10"),
        Petsitter("Petsitter 2", "https://i.pravatar.cc/150?img=20"),
        Petsitter("Petsitter 3", "https://i.pravatar.cc/150?img=30"),
        Petsitter("Petsitter 4", "https://i.pravatar.cc/150?img=40"),
        Petsitter("Petsitter 5", "https://i.pravatar.cc/150?img=50"),
        Petsitter("Petsitter 6", "https://i.pravatar.cc/150?img=60"),
        Petsitter("Petsitter 7", "https://i.pravatar.cc/150?img=70"),
        Petsitter("Petsitter 8", "https://i.pravatar.cc/150?img=8")
    )

    private var filteredList = itemList


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_Recherche)

        recyclerView = findViewById(R.id.recherche_recyclerView)
        searchView = findViewById(R.id.recherche_searchView)
        btnFilters = findViewById(R.id.recherche_filtre_button)
        btnSwitchView = findViewById(R.id.recherche_switchView_button)
        cardView = findViewById(R.id.recherche_cardView)

        setupRecyclerView()
        setupSearchView()
        setupButtons()
    }

    private fun setupRecyclerView() {
        adapter = ResultAdapter(filteredList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(searchView.windowToken, 0)
    }

    private fun setupSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                hideKeyboard()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filteredList = if (newText.isNullOrEmpty()) {
                    itemList
                } else {
                    itemList.filter { it.name.contains(newText, ignoreCase = true) }
                }
                adapter.updateList(filteredList)
                return true
            }

        })
    }



    private fun setupButtons() {
        btnSwitchView.setOnClickListener {
            isListView = !isListView
            if (isListView) {
                //vue liste => passer à vue carte
                recyclerView.visibility = View.VISIBLE
                cardView.visibility = View.GONE
                btnSwitchView.text = "Voir la carte"
            } else {
                //vue carte => passer à vue liste
                recyclerView.visibility = View.GONE
                cardView.visibility = View.VISIBLE
                btnSwitchView.text = "Voir la liste"
            }
        }

        btnFilters.setOnClickListener { view ->
            val popupMenu = PopupMenu(this, view)
            val menu = popupMenu.menu

            // Filtres :
            menu.add(0, 1, 0, "Note")
            menu.add(0, 2, 1, "Dates")
            menu.add(0, 3, 2, "Catégorie d'animal")
            menu.add(0, 4, 3, "Type de logement")

            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    1 -> Toast.makeText(this, "Filtre: Note", Toast.LENGTH_SHORT).show()
                    2 -> Toast.makeText(this, "Filtre: Dates", Toast.LENGTH_SHORT).show()
                    3 -> Toast.makeText(this, "Filtre: Catégorie d’animal", Toast.LENGTH_SHORT).show()
                    4 -> Toast.makeText(this, "Filtre: Type de logement", Toast.LENGTH_SHORT).show()
                }
                true
            }

            popupMenu.show()
        }
    }

    inner class ResultAdapter(private var dataList: List<Petsitter>) :
        RecyclerView.Adapter<ResultAdapter.ViewHolder>() {

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val textView: TextView = itemView.findViewById(R.id.recherche_petsitter_textView)
            val imageView: ImageView = itemView.findViewById(R.id.recherche_petsitter_imageView)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.recherche_view, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = dataList[position]
            holder.textView.text = item.name

            Glide.with(holder.itemView.context)
                .load(item.imageUrl)
                .dontAnimate()
                .placeholder(R.drawable.item)
                .into(holder.imageView)

        }

        override fun getItemCount(): Int = dataList.size

        fun updateList(newList: List<Petsitter>) {
            dataList = newList
            notifyDataSetChanged()
        }
    }


}

data class Petsitter(
    val name: String,
    val imageUrl: String
)

