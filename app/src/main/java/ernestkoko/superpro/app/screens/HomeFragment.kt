package ernestkoko.superpro.app.screens


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ernestkoko.superpro.app.R
import ernestkoko.superpro.app.databinding.HomeFragmentBinding

class HomeFragment : Fragment() {
    private lateinit var mBinding: HomeFragmentBinding
    private lateinit var viewModel: HomeViewModel


    companion object {
        fun newInstance() = HomeFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.home_fragment, container, false)

        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        //tell the binding class about the view model
        mBinding.homeViewModel = viewModel
        //setup the drawer layout
        //find the navController and set up the tool bar
        val navController = findNavController()
        val drawer = mBinding.drawerLayout

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

        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


    }

}