package ernestkoko.superpro.app.screens


import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import ernestkoko.superpro.app.R
import ernestkoko.superpro.app.databinding.HomeFragmentBinding

class HomeFragment : Fragment() {
    private lateinit var mBinding: HomeFragmentBinding
    private lateinit var viewModel: HomeViewModel
    private val TAG = "HomeFrag"


    companion object {
        fun newInstance() = HomeFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.home_fragment, container, false)
       //telling the firebase to persist data offline
//        Firebase.database.setPersistenceEnabled(true)
        //find the nav controller
        val navController = findNavController()
        val drawer = mBinding.drawerLayout
        val appBarConfig = AppBarConfiguration(setOf(R.id.homeFragment), drawer)
        //set up the toolbar
        mBinding.homeToolBar.setupWithNavController(navController, appBarConfig)
        // mBinding.navView.setupWithNavController(navController)


        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        //tell the binding class about the view model
        mBinding.homeViewModel = viewModel
        //setup the drawer layout
        //find the navController and set up the tool bar
//        val navController = findNavController()
//        val drawer = mBinding.drawerLayout


        // val appBarConfiguration = AppBarConfiguration(navController.graph, drawer)
        // .setupWithNavController(navController, appBarConfiguration)
        mBinding.navView.setupWithNavController(navController)


        mBinding.recyclerView.layoutManager =
            LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)
        val adapter = HomeFragmentAdapter()
        mBinding.recyclerView.adapter = adapter
        //listen for when to navigate to new product fragment
        viewModel.navToNewProduct.observe(viewLifecycleOwner, Observer { navToNewproduct ->
            if (navToNewproduct) {
                findNavController().navigate(R.id.action_homeFragment_to_newProductFragment)
                viewModel.doneNavToNewProduct()
            }
        })
        //observe when logout if clicked
        viewModel.isLogoutClicked.observe(viewLifecycleOwner, Observer { isLogoutClicked ->
            if (isLogoutClicked) {
                //pop up dialog
                popDialog()


                //set the value to false
                viewModel.doneClickingLogout()
            }
        })
        // This callback will only be called when MyFragment is at least Started.
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            // Handle the back button event
            popDialog()
        }
        viewModel.prod.observe(viewLifecycleOwner, Observer {
            it?.let {
               adapter.submitList(it)
            }
        })





        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


    }

    private fun popDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Want to log out?")
            .setPositiveButton("Ok", { dialogInterface: DialogInterface, i: Int ->
                Log.i(TAG, "Ok: Clicked")
                //log out the user
                FirebaseAuth.getInstance().signOut()
                //finish the activity
                requireActivity().finish()

            }).setNegativeButton("Cancel", { dialogInterface: DialogInterface, i: Int ->
                Log.i(TAG, "Cancel: Clicked")
            }).show()
    }


}