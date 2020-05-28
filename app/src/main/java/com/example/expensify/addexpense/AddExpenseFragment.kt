package com.example.expensify.addexpense

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.expensify.R
import com.example.expensify.databinding.AddExpenseFragmentBinding
import com.example.expensify.helper.Expense
import java.util.*

class AddExpenseFragment : Fragment() {

    companion object {
        const val TAG = "AddExpenseFragment"
    }

    private lateinit var binding: AddExpenseFragmentBinding

    private lateinit var viewModel: AddExpenseViewModel

    //private lateinit var fusedLocationClient: FusedLocationProviderClient
    //private val REQUEST_LOCATION_PERMISSION = 1
    //private var location: Location? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.add_expense_fragment, container, false)

        viewModel =
            ViewModelProvider(this, AddExpenseViewModelFactory(requireActivity().application)).get(
                AddExpenseViewModel::class.java
            )

        binding.addExpenseFab.setOnClickListener {
            var amount = binding.amountEdit.text.toString().toDouble()
            if (binding.expenseRadio.isChecked)
                amount *= -1.0

            if (binding.includeLocCheckbox.isChecked) {
                viewModel.saveExpense(
                    Expense(
                        amount,
                        binding.titleEdit.text.toString(),
                        binding.descEdit.text.toString(),
                        null, //TODO: Include GeoPoint here
                        Date()
                    )
                )
            } else {
                viewModel.saveExpense(
                    Expense(
                        amount,
                        binding.titleEdit.text.toString(),
                        binding.descEdit.text.toString(),
                        null,
                        Date()
                    )
                )
            }
            findNavController().navigate(R.id.action_addExpenseFragment_to_dashboardFragment)
        }

//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
//
//        if (isPermissionGranted()) {
//            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
//                this.location = location
//                Log.i(
//                    TAG,
//                    "Location latitude: ${location.latitude} and longitude: ${location.longitude}"
//                )
//            }
//        } else {
//            enableLocation()
//        }

        return binding.root
    }

//    private fun isPermissionGranted(): Boolean {
//        return checkSelfPermission(
//            requireContext(),
//            Manifest.permission.ACCESS_FINE_LOCATION
//        ) == PackageManager.PERMISSION_GRANTED
//    }
//
//    private fun enableLocation() {
//        requestPermissions(
//            requireActivity(),
//            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//            REQUEST_LOCATION_PERMISSION
//        )
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<String>,
//        grantResults: IntArray
//    ) {
//        if (requestCode == REQUEST_LOCATION_PERMISSION) {
//            if (grantResults.contains(PackageManager.PERMISSION_GRANTED)) {
//                enableLocation()
//            }
//        }
//    }

}
