/**
 * Created by longlv on 3/11/17.
 */

$(document).ready(function() {
	let types = [
	    "Send_Key",
        "Click",
        "Double_Click",
        "Drag_And_Drop"
    ], operators = [
        "Equal",
        "Contain",
        "Grid",
        "Enabled",
        "Disabled",
        "Selected"
    ];
	let columnDefs = [{
	    title: "Step", orderable: true, type: "numeric", width: "30px"
	  }, {
	    title: "Type Element", orderable: false,
        type: "dropdown",
        dataType: types, width: "10%"
	  }, {
	    title: "Element Name", orderable: false, width: "10%"
	  }, {
	    title: "Xpath", orderable: false, width: "40%"
	  }, {
	    title: "Value", orderable: false, width: "20%"
	  }
    ];
	
	let columnDefs2 = [{
	    title: "Step", orderable: false, type: "numeric"
	  }, {
	    title: "Element Name", orderable: false
	  }, {
        title: "Xpath", orderable: false
      }, {
	    title: "Actual", orderable: false, hidden: true
	  }, {
        title: "Operator", orderable: false, type: "dropdown",
        dataType: operators
      }, {
	    title: "Expected", orderable: false
	  }, {
	    title: "Result", orderable: false, hidden: true
	  }];

    let columnDefs3 = [{
        title: "id", name: "id", orderable: false, width: "50px"
    }, {
        title: "description", name: "description", orderable: false, width: "100px"
    }, {
        title: "url", name: "url", orderable: false, width: "100px"
    },{
        title: "user_action", name: "user_action", orderable: false, width: "200px"
    }, {
        title: "ui_result", name: "ui_result", orderable: false, width: "200px"
    }, {
        title: "sqls", name: "sqls", orderable: false, width: "100px"
    }, {
        title: "result", name: "result", orderable: false, width: "100px"
    }];
	
    let expectedResult = $('#expected_table').DataTable({
    	"columns": columnDefs2,
    	"paging":   false,
    	"bFilter": false,
    	"info":     false,
    	"searching": false,
        "columnDefs": [{
            targets: 6,
            render: function ( data, type, row ) {
                if (data != null) {
                    if (data.toLowerCase() == "fail") {
                        return "<b style='color:red'>" + data + "</b>";
                    } else {
                        return "<b style='color:green'>" + data + "</b>";
                    }
                } else {
                    return data;
                }
            }
        }],
        "dom": 'Bfrtip',
        "select": 'single',
        "altEditor": true,
        "buttons": [{
            text: 'Add',
            name: 'add'        // do not change name
        },
            {
                extend: 'selected',
                text: 'Edit',
                name: 'edit'        // do not change name
            },
            {
                extend: 'selected',
                text: 'Delete',
                name: 'delete'      // do not change name
            }]
    });
    let testCaseStep =  $('#test_case_table').DataTable({
    	"paging":   false,
    	"bFilter": false,
    	"info":     false,
    	"searching": false,
    	"columns": columnDefs,
    	"dom": 'Bfrtip',
    	"select": 'single',
        "altEditor": true,
        "buttons": [{
            text: 'Add',
            name: 'add'        // do not change name
          },
          {
        	extend: 'selected',
            text: 'Edit',
            name: 'edit'        // do not change name
          },
          {
        	extend: 'selected',
            text: 'Delete',
            name: 'delete'      // do not change name
         }]
    });
    let listTestCaseTable = $('#list_test_case').DataTable({
        autoWidth: false,
        columns: columnDefs3,
        dom: 'Bfrtip',
        buttons: [
            'csv', 'excel'
        ],
        "columnDefs": [{
            targets: 6,
            render: function ( data, type, row ) {
                if (data != null) {
                    if (data.toLowerCase() == "fail") {
                        return "<b style='color:red'>" + data + "</b>";
                    } else {
                        return "<b style='color:green'>" + data + "</b>";
                    }
                } else {
                    return data;
                }
            }
        }]
    });
    
    $("#try-to-run").on('click', () => {
        let data = buildObject();
        if (data.userActions.length ==0 || data.uiResults.length==0) {
            MessageUtils.errorMessage("Missing step in this test case. Please update test case again!");
            return false;
        }
        $.blockUI();
        new CallServiceAPI(location.origin).ajaxPost("/test", buildObject()).then((data) => {
            $.unblockUI();
            data = JSON.parse(data);
            let uiResults = data.uiResults, length = uiResults.length, finalResult = true;
            expectedResult.rows().remove();
            for (let i = 0; i < length; i++) {
                if (uiResults[i].result.toLowerCase() == "fail") {
                    finalResult = false;
                }
                expectedResult.row.add([i + 1, uiResults[i].name, uiResults[i].xpath, uiResults[i].actual, uiResults[i].operator, uiResults[i].expected, uiResults[i].result]).draw(false);
            }
            if (!finalResult) {
                $("#result-status").html("<b style='color: red'>FAIL</b>");
            } else {
                $("#result-status").html("<b style='color: green'>PASS</b>");
            }
        }, (error) => {
            console.log(error);
            $.unblockUI({
                onUnblock: function(){ MessageUtils.errorMessage("Test case run fail!"); }
            });
        })
    });
    $('#create-test-case-form').validator().on('submit', (e) => {
        if (e.isDefaultPrevented()) {
            MessageUtils.errorMessage('Missing or invalid field is existed. Please check again');
        } else {
            let testCase = buildObject(), apiService = $("#api_service"), saveButton = $("#save-test-case");
            if (!saveButton.attr("data-target") && checkExistedTestCase(testCase.id)) {
                MessageUtils.errorMessage("This test case is existed. You only are allowed updating it.");
                return false;
            }
            if (testCase.userActions.length ==0 || testCase.uiResults.length==0) {
                MessageUtils.errorMessage("Missing step in this test case. Please update test case again!");
                return false;
            }
            testCase.url = (apiService.val().startsWith("/")?apiService.val():"/"+apiService.val());
            if (!saveButton.attr("data-target")) {
                listTestCaseTable.row.add([testCase.id, testCase.description, testCase.url,
                    JSON.stringify(testCase.userActions), JSON.stringify(testCase.uiResults), testCase.sqls, null]).draw(false);
            } else {
                $("#cancel-edit-test-case").hide();
                saveButton.removeAttr("data-target");
                $("#test_case_name").val("");
                $("#description").val("");
                $("#api_service").val("");
                $("#sqls").val("");
                testCaseStep.rows().remove().draw();
                expectedResult.rows().remove().draw();
                listTestCaseTable.row(".selected").data([testCase.id, testCase.description, testCase.url,
                    JSON.stringify(testCase.userActions), JSON.stringify(testCase.uiResults), testCase.sqls, null]);
            }
            initialEvent();
        }
    });

    $('#editTestCase').click(() => {
        let selectedTestCase = listTestCaseTable.row(".selected");
        if (selectedTestCase.length > 0) {
            $('#list_test_case').find("tbody tr").off("click");
            $("#cancel-edit-test-case").show();
            $("#save-test-case").attr("data-target", "edit");
            testCaseStep.rows().remove().draw();
            expectedResult.rows().remove().draw();
            let data = selectedTestCase.data();
            //Insert data to 2 tables
            $("#test_case_name").val(data[0]);
            $("#description").val(data[1]);
            $("#api_service").val(data[2]);
            $("#sqls").val(data[5]);
            $("#result-status").html("");
            try {
                let listSteps = JSON.parse(data[3]),
                    listResult = JSON.parse(data[4]);

                if (listSteps.length > 0) {
                    for (let index in listSteps) {
                        let rowData = [listSteps[index].step, listSteps[index].inputType,
                            listSteps[index].name, listSteps[index].xpath, listSteps[index].value];
                        testCaseStep.row.add(rowData).draw(false);
                    }
                }
                if (listResult.length > 0) {
                    for (let index in listResult) {
                        let rowData = [parseInt(index)+1, listResult[index].name,
                            listResult[index].xpath, null, listResult[index].operator, listResult[index].expected, null];
                        expectedResult.row.add(rowData).draw(false);
                    }
                }
                $("#test_case_name").focus();
            } catch (e){
                console.log(e);
            }
        } else {
            MessageUtils.errorMessage("Please select a item to edit!");
        }
    });
    $('#deleteTestCase').click(() => {
        let selectedTestCase = listTestCaseTable.row(".selected");
        if (selectedTestCase.length > 0) {
            MessageUtils.confirmMessage("Are you want to delete this item? Please confirm...", function () {
                listTestCaseTable.row(".selected").remove().draw();
                $('#editTestCase').attr("disabled", "disabled");
                $('#deleteTestCase').attr("disabled", "disabled");
            });
        } else {
            MessageUtils.errorMessage("Please select a item to delete!");
        }
    });

    $('#cancel-edit-test-case').click(() => {
        $('#cancel-edit-test-case').hide();
        $("#save-test-case").removeAttr("data-target");
        $("#test_case_name").val("");
        $("#description").val("");
        $("#api_service").val("");
        $("#sqls").val("");
        testCaseStep.rows().remove().draw();
        expectedResult.rows().remove().draw();
        initialEvent();
    });

    $("#importTestCase").change((evt) => {
        $("#form-import-test-case").submit();
    });

    $('#runTestCases').click(() => {
        if ($("#ip_address").val() == "" || $("#port").val() == "") {
            MessageUtils.errorMessage("Missing IP Address or Port! Please fill valid data...");
            return false;
        }
        let allTestCases = listTestCaseTable.rows().data(), length = allTestCases.length;
        if (length < 1) {
            MessageUtils.errorMessage("No test case is ready to run! Please fill data into grid...");
            return false;
        }
        $.blockUI();
        let postData = {
            fileName: "mock_temp.xlsx",
            host: $("#ip_address").val(),
            port: parseInt($("#port").val()),
            testCases: []
        };
        for (let i = 0; i < allTestCases.length; i++) {
            postData.testCases.push(new UITestCaseTemplate(
                allTestCases[i][0],
                allTestCases[i][1],
                allTestCases[i][2],
                allTestCases[i][3],
                allTestCases[i][4],
                allTestCases[i][5],
                "",
                ""
            ))
        }
        $.ajax({
            type: "POST",
            url: "/test-all",
            data: JSON.stringify(postData),
            contentType: "application/json",
            success: function (data) {
                $.unblockUI();
                console.log("SUCCESS : ", data);
                if (data && data.length > 0) {
                    listTestCaseTable.rows().remove().draw();
                    for (let i in data) {
                        listTestCaseTable.row.add([
                            data[i].id,
                            data[i].description,
                            new URL(data[i].url).pathname,
                            JSON.stringify(data[i].userActions),
                            JSON.stringify(data[i].uiResults),
                            data[i].sqls,
                            data[i].result
                        ]).draw(false);
                    }
                    initialEvent();
                }
            },
            error: function (e) {
                $.unblockUI();
                console.log("ERROR : ", e);
            }
        });
    });

    $("#form-import-test-case").submit(function (event) {
        $.blockUI();
        //disable the default form submission
        event.preventDefault();
        let formData = new FormData($(this)[0]);
        $.ajax({
            type: "POST",
            enctype: 'multipart/form-data',
            url: "/import",
            data: formData,
            processData: false,
            contentType: false,
            cache: false,
            timeout: 600000,
            success: function (data) {
                $.unblockUI();
                $("#importTestCase").val("");
                console.log("SUCCESS : ", data);
                listTestCaseTable.rows().remove().draw();
                if (data && data.length > 0) {
                    for (let index in data) {
                        let rowData =[data[index].id, data[index].description, data[index].url,
                            JSON.stringify(data[index].userActions), JSON.stringify(data[index].uiResults), data[index].sqls, data[index].result];
                        listTestCaseTable.row.add(rowData).draw(false);
                    }
                    initialEvent();
                }
            },
            error: function (e) {
                $.unblockUI();
                console.log("ERROR : ", e);
                $("#importTestCase").val("");
            }
        });
        return false;
    });

    let initialEvent = () => {
        let idLs = '#list_test_case';
        $(idLs).find("tbody tr").off("click");
        $('#list_test_case').find("tbody tr").on('click', (evt) => {
            let target = evt.currentTarget;
            if ( $(target).hasClass('selected') ) {
                $(target).removeClass('selected');
                $('#editTestCase').attr("disabled", "disabled");
                $('#deleteTestCase').attr("disabled", "disabled");
            }
            else {
                $(idLs).find("tr.selected").removeClass('selected');
                $(target).addClass('selected');
                $('#editTestCase').removeAttr("disabled");
                $('#deleteTestCase').removeAttr("disabled");
            }
        });
    };

    let checkExistedTestCase = (testCaseCode) => {
        let listTestCase = listTestCaseTable.rows().data();
        return listTestCase.filter((t) => t[0] == testCaseCode).length != 0;
    };

    let buildObject = () => {
        let apiService = $("#api_service"), testCase = {
            id: $("#test_case_name").val(),
            description: $("#description").val(),
            url: "http://" + $("#ip_address").val() + ":" + $("#port").val() +
                (apiService.val().startsWith("/")?apiService.val():"/"+apiService.val()),
            sqls: $("#sqls").val(),
            userActions: [],
            uiResults: []
        };
        let listSteps = testCaseStep.data();
        let listExpected = expectedResult.data();

        for(let i = 0; i < listSteps.length; i++) {
            let step = listSteps[i];
            let object = {};
            object.step = step[0];
            object.inputType = step[1].toLowerCase();
            object.name = step[2];
            object.xpath = step[3];
            object.value = step[4];
            testCase.userActions.push(object);
        }
        for(let i = 0; i < listExpected.length; i++) {
            let step = listExpected[i];
            let object = {};
            object.name = step[1];
            object.xpath = step[2];
            object.operator = step[4].toLowerCase();
            object.expected = step[5];
            testCase.uiResults.push(object);
        }
        return testCase;
    };

    class UITestCaseTemplate {
        constructor(id, description, url, userActions, uiResults, sqls, result, message) {
            this.id = id;
            this.description = description;
            this.url = url;
            this.userActions = [];
            this.uiResults = [];
            try {
                let listSteps = JSON.parse(userActions);
                if (listSteps && listSteps.length) {
                    for (let i in listSteps) {
                        this.userActions.push(new UserAction(listSteps[i].step, listSteps[i].inputType, listSteps[i].name,
                            listSteps[i].xpath, listSteps[i].value));
                    }
                }
                let listExpected = JSON.parse(uiResults);
                if (listExpected && listExpected.length) {
                    for (let i in listExpected) {
                        this.uiResults.push(new UIResult(listExpected[i].name, listExpected[i].xpath, listExpected[i].operator,
                            listExpected[i].expected, listExpected[i].actual, listExpected[i].result));
                    }
                }
            } catch (e) {
                console.log(e);
            }

            this.sqls = sqls;
            this.result = result;
            this.message = message;
        }

    }

    class UserAction {
        constructor(step, inputType, name, xpath, value) {
            this.step = step;
            this.inputType = inputType;
            this.name = name;
            this.xpath = xpath;
            this.value = value;
        }
    }

    class UIResult {
        constructor(name, xpath, operator, expected, actual, result) {
            this.name = name;
            this.xpath = xpath;
            this.operator = operator;
            this.expected = expected;
            this.actual = actual;
            this.result = result;
        }
    }

    class CallServiceAPI {
        constructor(url) {
            this.url = url;
        }
        ajaxPost(api, data) {
            let url = this.url + api;
            return new Promise(function(resolve, reject) {
                let req = new XMLHttpRequest();
                req.open("POST", url, true);
                req.setRequestHeader("Content-Type", "application/json");
                req.onload = function() {
                    if (req.status === 200) {
                        resolve(req.response);
                    } else {
                        reject(new Error(req.statusText));
                    }
                };
                req.onerror = function() {
                    reject(new Error("Network error"));
                };

                req.send(JSON.stringify(data));
            });
        }
    }
    class MessageUtils {
        static errorMessage(message) {
            BootstrapDialog.show({
                title: 'Error Message',
                message: message,
                buttons: [{
                    label: 'Close',
                    action: function(dialog) {
                        dialog.close();
                    }
                }]
            });
        }

        static confirmMessage(message, fn) {
            BootstrapDialog.show({
                title: 'Confirm Message',
                message: message,
                buttons: [{
                    label: 'Cancel',
                    action: function(dialog) {
                        dialog.close();
                    }
                }, {
                    label: 'OK',
                    action: function(dialog) {
                        if (fn) {
                            fn();
                        }
                        dialog.close();
                    }
                }]
            });
        }
    }
});