import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

//*********************************************************
// Code created on: 5/13/2019
// Code last edited on: 5/17/2019
// Code created by: Spencer Raymond
// Associated with: MSU CSCI-232
// Description: Binary tree creator. Takes in an input file
// named input.txt with integer values separated by ","
// an example of an input would be (5,4,6,3,7,2,8). The
// output of this code is a UI where you can print the tree
// in a form that can be easily read, print the Pre-Order,
// Post-Order, In-Order traversals, input new values,
// delete unwanted values, and search for specific values
// in the tree. I wanted to make this as user-friendly as
// possible, so also included are try methods and catching
// input mismatch exceptions with proper handling.
//*********************************************************

class Main {
    Node root;

    Main() {
    	root = null;
    }
    
    class Node {
		int value;
        Node left, right, parent;
        
        public Node(int i) {
            value = i;
            left = right = parent = null;
        }
    }
    
    void printTree() {
    	print(root, 0);
    }
    // This function prints the tree recursively and vertically
    // instead of printing iteratively horizontally
    void print(Node root, int spacing) {
    	if (root == null) {
    		return;
    	}
    	int factorOfSpacing = 7;
        spacing = factorOfSpacing + spacing;
        print(root.right, spacing);
    	for (int i = factorOfSpacing; i < spacing; i++) {
    		System.out.print(" ");
    	}
    	System.out.println(root.value);
    	print(root.left, spacing);
    }
    
    int findDepth(Node root) {
    	if (root == null) {
    		return 0;
    	}else if (root.left == null && root.right == null) {
    		return 1;
    	}else {
    		int left = findDepth(root.left);
    		int right = findDepth(root.right);
    		return (Math.max(left, right)+1);
    	}
    }
    
    void insertNew(int value) {
       root = insert(root, value);
    }

    Node insert(Node root, int value) {
        if (root == null) {
            root = new Node(value);
            return root;
        }else if (value < root.value) {
        	root.left = insert(root.left, value);
        	root.left.parent = root;
        }else if (value > root.value) {
            root.right = insert(root.right, value);
            root.right.parent = root;
        }
        return root;
    }
    
    boolean findValue(int value) {
    	Node node = findValueTraversal(root, value);
    	if(node != null) {
    		return true;
    	} else {
    		return false;
    	}
    }

    public Node findValueTraversal(Node root, int value) {
        if (root != null) {
        	if(root.value == value) {
        		return root;
        	}else if (root.value > value) {
        		return findValueTraversal(root.left, value);
        	}else {
        		return findValueTraversal(root.right, value);
        	}
        }else {
        	return root;
        }
    }
    
    boolean delete(int deleteVal) {
    	Node node = findValueTraversal(root, deleteVal);
    	if(node != null) {
    		// case 1 ; no children
    		if(node.left == null && node.right == null) {
    			if(node == root) {
    				root = null;
    			}else if(node.parent.value > node.value) {
    				node.parent.left = null;
    			} else {
    				node.parent.right = null;
    			}
    		}else if (node.right == null && node.left != null) { // case 2 ; 1 left child
    			if(node == root) {
    				root = node.left;
    				root.parent = null;
    			}else {
    				if(node.parent.value > node.value) {
    					node.left.parent = node.parent;
    					node.parent.left = node.left;
    				}else {
    					node.left.parent = node.parent;
    					node.parent.right = node.left;
    				}
    			}
    		}else if (node.right != null && node.left == null) { // case 2 ; 1 right child
    			if(node == root) {
    				root = node.right;
    				root.parent = null;
    			}else {
    				if(node.parent.value > node.value) {
    					node.right.parent = node.parent;
    					node.parent.left = node.right;
    				}else {
    					node.right.parent = node.parent;
    					node.parent.right = node.right;
    				}
    			}
    		}else if (node.right != null && node.left != null) { // case 3 ; 2 children
    			Node succ = findSucc(node);
    			if(succ == node.right) { // easy case where the successor is the root of the right subtree
    				if(node == root) {
    					succ.left = root.left;
    					succ.left.parent = succ;
    					succ.parent = null;
    					root = succ;
    				}else {
    					succ.left = node.left;
    					succ.left.parent = succ;
    					if(node.parent.value < succ.value) {
    						node.parent.right = succ;
    					}else {
    						node.parent.left = succ;
    					}
    					succ.parent = node.parent;
    				}
    			}else {
    				if(node == root) {
    					succ.parent.left = succ.right;
    					succ.left = root.left;
    					succ.left.parent = succ;
    					succ.right = root.right;
    					succ.right.parent = succ;
    					succ.parent = null;
    					root = succ;
    				} else {
    					succ.parent.left = succ.right;
    					succ.right = node.right;
    					succ.right.parent = succ;
    					succ.left = node.left;
    					succ.left.parent = succ;
    					succ.parent = node.parent;
    					if(node.parent.value < succ.value) {
    						succ.parent.right = succ;
    					}else {
    						succ.parent.left = succ;
    					}
    					succ.parent = node.parent;
    				}
    			}
    		}
    		node.left = null;
			node.right = null;
			node.parent = null;
    		return true;
    	} else {
    		return false;
    	}
    }
    
    Node findSucc(Node node) {
    	Node succ = null;
    	Node temp = node.right;
        while(succ == null) {
        	if(temp.left != null) {
        		temp = temp.left;
        	}else {
        		succ = temp;
        	}
        }
    	return succ;
    }
    
    void inOrder() {
        inOrderTraversal(root);
    }
    
    void postOrder() {
    	postOrderTraversal(root);
    }
    
    void preOrder() {
    	preOrderTraversal(root);
    }

    void inOrderTraversal(Node root) {
        if (root != null) {
        	inOrderTraversal(root.left);
            System.out.println(root.value);
            inOrderTraversal(root.right);
        }
    }
    
    void postOrderTraversal(Node root) {
    	if (root != null) {
    		postOrderTraversal(root.left);
    		postOrderTraversal(root.right);
            System.out.println(root.value);
    	}
    }
    
    void preOrderTraversal(Node root) {
    	if (root != null) {
    		System.out.println(root.value);
    		preOrderTraversal(root.left);
    		preOrderTraversal(root.right);
    	}
    }

    public static void main(String[] args) throws FileNotFoundException {
        Main tree = new Main();
		Scanner s = new Scanner(new File("input.txt")).useDelimiter("\\s*,\\s*");
		while(s.hasNext()) {
			tree.insertNew(s.nextInt());
		}
	    s.close();
	    boolean running = true;
	    
	    while(running) {
	    	int choice = 0;
	    	Scanner read = new Scanner(System.in);
	    	System.out.println("1: Print tree 2: Pre-Order 3: In-Order 4:Post-Order\n"
	    			+ "5: Delete A Value 6: Find A Value 7: Insert New Value\n");
	    	try{
	    		choice = read.nextInt();
	    	}catch (java.util.InputMismatchException e) {System.out.println("Please select from the following list\n"); continue;}
	    	switch(choice) {
	    	case 1:
	    		System.out.println("Printing...");
	    		System.out.println("--------------------********--------------------\n");
	    		tree.printTree();
	    		System.out.println("\n--------------------********--------------------\n");
	    		break;
	    	case 2:
	    		System.out.println("\nThe Pre-Order Tree\n");
	    		tree.preOrder();
	    		System.out.println();
	    		break;
	    	case 3:
	    		System.out.println("\nThe In-Order Tree\n");
	    		tree.inOrder();
	    		System.out.println();
	    		break;
	    	case 4:
	    		System.out.println("\nThe Post-Order Tree\n");
	    		tree.postOrder();
	    		System.out.println();
	    		break;
	    	case 5:
	    		Scanner deleteVal = new Scanner(System.in);
		    	System.out.println("\nWhat value would you like to delete in the tree?\n");
	    		if(tree.delete(deleteVal.nextInt())){
	    			System.out.println("\nAttempting to Delete Value...\n\nValue Deleted\n");
	    		}else {
	    			System.out.println("\nAttempting to Delete Value...\n\nValue Not Detected\n");
	    		}
	    		break;
	    	case 6:
	    		Scanner whatVal = new Scanner(System.in);
		    	System.out.println("What value would you like to check in the tree?\n");
		    	int val = whatVal.nextInt();
	    		if (tree.findValue(val)) {
	    			System.out.println("Value Found\n");
	    		}else {
	    			System.out.println("Value Not Found\n");
	    		}
	    		break;
	    	case 7:
	    		Scanner newVal = new Scanner(System.in);
		    	System.out.println("What value would you like to add to the tree?\n");
		    	tree.insertNew(newVal.nextInt());
		    	break;
	    	default:
	    		System.out.println("Please select from the following list\n");
	    		break;
	    	}
	    }
    } 
}