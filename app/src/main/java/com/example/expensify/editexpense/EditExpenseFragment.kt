package com.example.expensify.editexpense

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.expensify.R
import com.example.expensify.databinding.EditExpenseFragmentBinding
import com.example.expensify.helper.Expense
import com.example.expensify.viewexpense.ViewExpenseFragmentArgs
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.GeoPoint
import java.util.*
import kotlin.math.abs

class EditExpenseFragment : Fragment() {

    private lateinit var binding: EditExpenseFragmentBinding

    private lateinit var viewModel: EditExpenseViewModel

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var geoPoint: GeoPoint? = null

    private val REQUEST_LOCATION_PERMISSION = 1
    private var canRequestLocation = false

    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.edit_expense_fragment, container, false)

        viewModel =
            ViewModelProvider(this, EditExpenseViewModelFactory(requireActivity().application)).get(
                EditExpenseViewModel::class.java
            )

        val args: ViewExpenseFragmentArgs by navArgs()
        val expense = args.expense

        val currentGeoPoint = expense.geoPoint
        if (currentGeoPoint != null) {
            with(binding.expenseLocationMap) {
                onCreate(null)
                getMapAsync {
                    com.google.android.gms.maps.MapsInitializer.initialize(context)
                    setMapLocation(
                        it,
                        LatLng(
                            currentGeoPoint.latitude,
                            currentGeoPoint.longitude
                        )
                    )
                }
            }
        } else {
            binding.expenseLocationMap.visibility = View.GONE
        }

        if (expense.amount >= 0.0)
            binding.incomeRadio.isChecked = true
        else
            binding.expenseRadio.isChecked = true

        binding.amountEdit.setText(abs(expense.amount).toString())
        binding.titleEdit.setText(expense.merchant)
        binding.descEdit.setText(expense.description)
        binding.expenseDateText.text = viewModel.formatDate(expense.date)

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

        binding.addExpenseFab.setOnClickListener {
            var amount = binding.amountEdit.text.toString().toDouble()
            if (binding.expenseRadio.isChecked)
                amount *= -1.0

            if (binding.includeLocCheckbox.isChecked) {
                if (canRequestLocation) {
                    if (geoPoint != null) {
                        viewModel.editExpense(
                            expense,
                            Expense(
                                amount,
                                binding.titleEdit.text.toString(),
                                binding.descEdit.text.toString(),
                                geoPoint,
                                Date()
                            )
                        )
                        findNavController().navigate(R.id.action_editExpenseFragment_to_dashboardFragment)
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
                viewModel.editExpense(
                    expense,
                    Expense(
                        amount,
                        binding.titleEdit.text.toString(),
                        binding.descEdit.text.toString(),
                        null,
                        Date()
                    )
                )
                findNavController().navigate(R.id.action_editExpenseFragment_to_dashboardFragment)
            }
        }

        return binding.root
    }

    private fun setMapLocation(map: GoogleMap, location: LatLng) {
        with(map) {
            moveCamera(CameraUpdateFactory.newLatLngZoom(location, 16f))
            addMarker(MarkerOptions().position(location))
            uiSettings.isScrollGesturesEnabled = false
            mapType = GoogleMap.MAP_TYPE_NORMAL
        }
    }

    private fun locationPermissionIsGranted(): Boolean {
        canRequestLocation = ContextCompat.checkSelfPermission(
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

    override fun onResume() {
        super.onResume()
        if (binding.expenseLocationMap.visibility != View.GONE)
            binding.expenseLocationMap.onResume()
    }

    override fun onPause() {
        super.onPause()
        if (binding.expenseLocationMap.visibility != View.GONE)
            binding.expenseLocationMap.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (binding.expenseLocationMap.visibility != View.GONE)
            binding.expenseLocationMap.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        if (binding.expenseLocationMap.visibility != View.GONE)
            binding.expenseLocationMap.onLowMemory()
    }

}