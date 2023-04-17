package test

import (
    "fmt"
    "strings"
	"testing"
    //"time"

    //http_helper "github.com/gruntwork-io/terratest/modules/http-helper"
    
    "github.com/gruntwork-io/terratest/modules/aws"
    "github.com/gruntwork-io/terratest/modules/random"
	"github.com/gruntwork-io/terratest/modules/terraform"
    "github.com/stretchr/testify/assert"
	//"github.com/gruntwork-io/terratest/modules/k8s"
)

func cleanS3(t *testing.T, awsRegion string, bucketName string) {
    aws.EmptyS3Bucket(t, awsRegion, bucketName)
    aws.DeleteS3Bucket(t, awsRegion, bucketName)
}

func TestTfAlineFinancial(t *testing.T) {

    //awsRegion := aws.GetRandomStableRegion(t, nil, nil) 
    awsRegion := "us-east-1" 
    randomNumber := strings.ToLower(random.UniqueId())
    projectName := "ik-aline-terratest-" + randomNumber
    
    defer cleanS3(t, awsRegion, projectName)
    aws.CreateS3Bucket(t, awsRegion, projectName)

	// Construct the terraform options with default retryable errors to handle the most common
	// retryable errors in terraform testing.
	terraformOptions := terraform.WithDefaultRetryableErrors(t, &terraform.Options{
		// Set the path to the Terraform code that will be tested.
		TerraformDir: "../resources/",
        Reconfigure: true,
        Vars: map[string]interface{}{
            "vpc_name": projectName ,
            "vpc_availability_zones": "[\""+awsRegion+"a\", \"" + awsRegion + "b\"]",
            "cluster_name": projectName ,
            "eks_desired_nodes": 1,
            "rds_name": projectName,
            "aws_region": awsRegion,
            "in_testing": true,
            
            "autoscale": false,
            "loadbalancer": false,
            "monitoring": false,
            "install_application": false,

        },
        BackendConfig: map[string]interface{}{
            "bucket": projectName,
            "key": fmt.Sprintf("%s/terraform.tfstate", randomNumber),
            "region": awsRegion,
        },
	})

	// Clean up resources with "terraform destroy" at the end of the test.
    defer terraform.Destroy(t, terraformOptions)

	// Run "terraform init" and "terraform apply". Fail the test if there are any errors.
	terraform.InitAndApply(t, terraformOptions)

    // Vpc Tests
    vpcId := terraform.Output(t, terraformOptions, "vpc_id")
    vpc := aws.GetVpcById(t, vpcId, awsRegion)
    vpcCidr := terraform.Output(t, terraformOptions, "vpc_cidr_block")
    dbSubnetsCidr := terraform.Output(t, terraformOptions, "database_subnets_cidr")
    publicSubnetsCidr := terraform.Output(t, terraformOptions, "public_subnets_cidr")
    privateSubnetsCidr := terraform.Output(t, terraformOptions, "private_subnets_cidr")

    assert.Equal(t, projectName, vpc.Name)
    assert.Equal(t, "10.0.0.0/16", vpcCidr)
    assert.Equal(t, "[10.0.0.0/19 10.0.32.0/19]", privateSubnetsCidr)
    assert.Equal(t, "[10.0.64.0/19 10.0.96.0/19]", publicSubnetsCidr)
    assert.Equal(t, "[10.0.128.0/19 10.0.160.0/19]", dbSubnetsCidr)

    // Rds Tests
    dbId := terraform.Output(t, terraformOptions, "db_id")
    dbEngine := terraform.Output(t, terraformOptions, "db_engine")
    dbName := terraform.Output(t, terraformOptions, "db_name")
    dbUsername := terraform.Output(t, terraformOptions, "db_username")
    dbAddress := aws.GetAddressOfRdsInstance(t, dbId, awsRegion)
    dbPort := aws.GetPortOfRdsInstance(t, dbId, awsRegion)

    assert.Equal(t, "mysql", dbEngine)
    assert.Equal(t, "aline", dbName)
    assert.Equal(t, "admin", dbUsername)
    assert.Equal(t, int64(3306), dbPort)
    assert.NotNil(t, dbAddress)

    // Eks Tests
    eksClusterVersion := terraform.Output(t, terraformOptions, "eks_cluster_version")
    eksClusterStatus := terraform.Output(t, terraformOptions, "eks_cluster_status")
    eksName := terraform.Output(t, terraformOptions, "eks_name")

    assert.Equal(t, "1.24", eksClusterVersion)
    assert.Equal(t, "ACTIVE", eksClusterStatus)
    assert.Equal(t, projectName, eksName)

    // Test on the application
    /*
    kubectlOptions := k8s.NewKubectlOptions("", "./kubeconfig", "microservices")
    
    ingress := k8s.GetIngress(t, kubectlOptions, "gateway-ingress")
    url := "http://" + ingress.Status.LoadBalancer.Ingress[0].Hostname
    
    fmt.Printf("Waiting for Application to install\n")
    time.Sleep(3*time.Minute)
    
    responseCode,_ := http_helper.HttpGet(t, url+"/applications", nil)
    assert.Equal(t,int(403), responseCode)
    responseCode,_ = http_helper.HttpGet(t, url+"/users", nil)
    assert.Equal(t,int(403), responseCode)
    responseCode,_ = http_helper.HttpGet(t, url+"/cards", nil)
    assert.Equal(t,int(403), responseCode)
    responseCode,_ = http_helper.HttpGet(t, url+"/transactions", nil)
    assert.Equal(t,int(403), responseCode)
    responseCode,_ = http_helper.HttpGet(t, url+"/banks", nil)
    assert.Equal(t,int(200), responseCode)
    responseCode,_ = http_helper.HttpGet(t, url+"/accounts", nil)
    assert.Equal(t,int(403), responseCode)
    */
}
