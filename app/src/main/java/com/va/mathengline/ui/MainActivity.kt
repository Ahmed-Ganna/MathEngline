package com.va.mathengline.ui

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.va.mathengline.R
import com.va.mathengline.databinding.ActivityMainBinding
import com.va.mathengline.databinding.ItemInputBinding
import com.va.mathengline.models.MathOperationItem
import com.va.mathengline.services.EngineService
import java.util.*
import kotlin.collections.ArrayList
import androidx.recyclerview.widget.DividerItemDecoration
import com.va.mathengline.utils.MathOperator
import com.yayandroid.locationmanager.LocationManager
import com.yayandroid.locationmanager.configuration.Configurations
import com.yayandroid.locationmanager.configuration.LocationConfiguration
import com.yayandroid.locationmanager.constants.ProcessType
import com.yayandroid.locationmanager.helper.StringUtils
import com.yayandroid.locationmanager.listener.LocationListener


class MainActivity : AppCompatActivity(), LocationListener {

    private lateinit var locationManager: LocationManager

    private var selectedOperator: MathOperator? = null

    private val mPendingAdapter = OperationsAdapter()
    private val mCompletedAdapter = OperationsAdapter()

    private lateinit var mBinding: ActivityMainBinding

    private lateinit var mService: EngineService
    private var mBound: Boolean = false

    private val connection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as EngineService.LocalBinder
            mService = binder.getService()
            mBound = true
            observeData()
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setUpSpinner()
        setOnClickListeners()
        setUpRvs()
        startService()
        requestLocation()
    }



    private fun startService(){
        ContextCompat.startForegroundService(this,Intent(this, EngineService::class.java))

    }

    private fun setUpRvs() {
        mBinding.rvPending.adapter = mPendingAdapter
        mBinding.rvCompleted.adapter = mCompletedAdapter

        mBinding.rvPending.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
        mBinding.rvCompleted.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
    }

    private fun observeData() {
        mService.pendingLveData.observe(this, {
            mPendingAdapter.items = it as ArrayList<MathOperationItem>
            mPendingAdapter.notifyDataSetChanged()
        })
        mService.completedLiveData.observe(this, {
            mCompletedAdapter.items = it as ArrayList<MathOperationItem>
            mCompletedAdapter.notifyDataSetChanged()
        })
    }

    private fun setOnClickListeners() {
        mBinding.btnAddParameter.setOnClickListener {
            addInput()
        }
        mBinding.btnSubmit.setOnClickListener {
            val parameters = mBinding.inputsContainer.children.map { (it as EditText).text.toString().toInt() }.toList()

            val delayTime = mBinding.inputDelay.text.toString().toLongOrNull()

            if (parameters.isNotEmpty() && delayTime !=null && delayTime !=0L){
                val operation = MathOperationItem(UUID.randomUUID().toString(),selectedOperator!!,delayTime,parameters)
                if (mBound){
                    mService.addOperation(operation)
                    clearInputs()
                }else{
                    Toast.makeText(this, "Service is not bounded", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this, "Validation error", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun addInput() {
        val binding = ItemInputBinding.inflate(LayoutInflater.from(this))
        mBinding.inputsContainer.addView(binding.root)
    }

    private fun clearInputs(){
        mBinding.inputsContainer.removeAllViews()
        mBinding.inputDelay.setText("")
    }


    private fun setUpSpinner() {
        val list = listOf(
            MathOperator.ADD, MathOperator.SUB, MathOperator.MUL, MathOperator.DIV
        )
        ArrayAdapter(this,android.R.layout.simple_spinner_item,list.map { it.text }).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            mBinding.spOperator.adapter = it
        }

        mBinding.spOperator.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedOperator = list[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

    }

    override fun onStart() {
        super.onStart()
        // Bind to LocalService
        Intent(this, EngineService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
        mBound = false
    }




    private fun requestLocation() {
        locationManager = LocationManager.Builder(this.applicationContext)
            .configuration(Configurations.defaultConfiguration(
                "Enable location",
                "Gps location is needed"))
            .activity(this)
            .notify(this)
            .build()
        locationManager.get()
    }

    override fun onDestroy() {
        locationManager.onDestroy()
        super.onDestroy()
    }

    override fun onPause() {
        locationManager.onPause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        locationManager.onResume()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        locationManager.onActivityResult(requestCode, resultCode, data)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,  grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        locationManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }



    override fun onProcessTypeChanged(processType: Int) {}

    override fun onLocationChanged(location: Location?) {
        mBinding.tvGpsInfo.text = "${location?.latitude} , ${location?.longitude}"
    }

    override fun onLocationFailed(type: Int) {
        mBinding.tvGpsInfo.text = "Failed $type"
    }


    override fun onPermissionGranted(alreadyHadPermission: Boolean) {}

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

    override fun onProviderEnabled(provider: String?) {}

    override fun onProviderDisabled(provider: String?) {}

}