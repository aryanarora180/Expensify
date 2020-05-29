package com.example.expensify.viewexpense

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.expensify.R
import com.example.expensify.databinding.ViewExpenseFragmentBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ViewExpenseFragment : Fragment() {

    private lateinit var binding: ViewExpenseFragmentBinding

    private lateinit var viewModel: ViewExpenseViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.view_expense_fragment, container, false)

        viewModel =
            ViewModelProvider(this, ViewExpenseViewModelFactory(requireActivity().application)).get(
                ViewExpenseViewModel::class.java
            )

        val args: ViewExpenseFragmentArgs by navArgs()
        val expense = args.expense

        val geoPoint = expense.geoPoint
        if(geoPoint != null) {
            with(binding.expenseLocationMap) {
                onCreate(null)
                getMapAsync {
                    MapsInitializer.initialize(context)
                    setMapLocation(it, LatLng(geoPoint.latitude, geoPoint.longitude))
                }
            }
        } else {
            binding.expenseLocationMap.visibility = View.GONE
        }

        binding.expenseAmountText.text = viewModel.formatAmount(expense.amount)
        binding.expenseDateText.text = viewModel.formatDate(expense.date)
        binding.expenseMerchantText.text = expense.merchant
        binding.expenseDescText.text = expense.description

        binding.deleteImage.setOnClickListener {
            MaterialAlertDialogBuilder(context)
                .setTitle(resources.getString(R.string.delete_expense_title))
                .setMessage(resources.getString(R.string.delete_expense_message))
                .setNegativeButton(resources.getString(R.string.delete_expense_no)) { dialog, which ->
                    //Do nothing
                }
                .setPositiveButton(resources.getString(R.string.delete_expense_yes)) { dialog, which ->
                    viewModel.deleteExpense(expense)
                    findNavController().navigate(R.id.action_viewExpenseFragment_to_dashboardFragment)
                    //Navigate back
                }
                .show()
        }

        return binding.root
    }

    private fun setMapLocation(map : GoogleMap, location: LatLng) {
        with(map) {
            moveCamera(CameraUpdateFactory.newLatLngZoom(location, 16f))
            addMarker(MarkerOptions().position(location))
            uiSettings.isScrollGesturesEnabled = false
            mapType = GoogleMap.MAP_TYPE_NORMAL
        }
    }

    override fun onResume() {
        super.onResume()
        if(binding.expenseLocationMap.visibility != View.GONE)
            binding.expenseLocationMap.onResume()
    }

    override fun onPause() {
        super.onPause()
        if(binding.expenseLocationMap.visibility != View.GONE)
            binding.expenseLocationMap.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        if(binding.expenseLocationMap.visibility != View.GONE)
            binding.expenseLocationMap.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        if(binding.expenseLocationMap.visibility != View.GONE)
            binding.expenseLocationMap.onLowMemory()
    }

}
