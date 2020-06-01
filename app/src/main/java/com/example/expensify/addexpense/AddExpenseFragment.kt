package com.example.expensify.addexpense

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.expensify.R
import com.example.expensify.databinding.AddExpenseFragmentBinding
import com.example.expensify.helper.Expense
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.GeoPoint
import java.util.*

class AddExpenseFragment : Fragment() {

    companion object {
        const val TAG = "AddExpenseFragment"
    }

    private lateinit var binding: AddExpenseFragmentBinding

    private lateinit var viewModel: AddExpenseViewModel

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var geoPoint: GeoPoint? = null
    private var date = Date()

    private val REQUEST_LOCATION_PERMISSION = 1
    private var canRequestLocation = false

    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.add_expense_fragment, container, false)

        viewModel =
            ViewModelProvider(this, AddExpenseViewModelFactory(requireActivity().application)).get(
                AddExpenseViewModel::class.java
            )

        binding.dateText.text = viewModel.formatDate(date)

        binding.includeLocCheckbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                if (locationPermissionIsGranted()) {
                    fusedLocationClient =
                        LocationServices.getFusedLocationProviderClient(requireActivity())
                    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                        if (location != null) {
                            geoPoint = GeoPoint(location.latitude, location.longitude)
                        }
                    }
                } else {
                    requestPermission()
                }
            }
        }

        val picker: MaterialDatePicker<Long> =
            MaterialDatePicker.Builder.datePicker().build().apply {
                addOnPositiveButtonClickListener {
                    date = Date(it)
                    binding.dateText.text = viewModel.formatDate(date)
                }
            }

        binding.editDateImage.setOnClickListener {
            picker.show(parentFragmentManager, picker.toString())
        }

        binding.addExpenseFab.setOnClickListener {
            var amount = binding.amountEdit.text.toString().toDouble()
            if (binding.expenseRadio.isChecked)
                amount *= -1.0

            if (binding.includeLocCheckbox.isChecked) {
                if (canRequestLocation) {
                    if (geoPoint != null) {
                        viewModel.saveExpense(
                            Expense(
                                amount,
                                binding.titleEdit.text.toString(),
                                binding.descEdit.text.toString(),
                                geoPoint,
                                date
                            )
                        )
                        findNavController().navigate(R.id.action_addExpenseFragment_to_dashboardFragment)
                    } else {
                        Snackbar.make(
                            binding.addExpenseCoordinator,
                            "Waiting for location...",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Snackbar.make(
                        binding.addExpenseCoordinator,
                        "Location permission not granted.",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            } else {
                viewModel.saveExpense(
                    Expense(
                        amount,
                        binding.titleEdit.text.toString(),
                        binding.descEdit.text.toString(),
                        null,
                        date
                    )
                )
                findNavController().navigate(R.id.action_addExpenseFragment_to_dashboardFragment)
            }
        }
        return binding.root
    }

    private fun locationPermissionIsGranted(): Boolean {
        canRequestLocation = checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        return canRequestLocation
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_LOCATION_PERMISSION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            canRequestLocation = grantResults.contains(PackageManager.PERMISSION_GRANTED)
        }
    }

}
